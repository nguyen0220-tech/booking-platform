package com.catholic.ac.kr.booking_platform.service;

import com.catholic.ac.kr.booking_platform.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.dto.PendingEmailUpdate;
import com.catholic.ac.kr.booking_platform.dto.ProfileDTO;
import com.catholic.ac.kr.booking_platform.dto.request.UpdateProfileRequest;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.enumdef.UpdateProfileType;
import com.catholic.ac.kr.booking_platform.event.UpdateProfileEvent;
import com.catholic.ac.kr.booking_platform.exception.AlreadyExistsException;
import com.catholic.ac.kr.booking_platform.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.mapper.UserMapper;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import com.catholic.ac.kr.booking_platform.cache.UpdateProfileCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

@Slf4j
@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final UploadHandler uploadHandler;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final UpdateProfileCacheService updateProfileCacheService;

    private final Map<UpdateProfileType, BiFunction<User, String, ApiResponse<String>>> handlerMap = Map.of(
            UpdateProfileType.FULL_NAME, this::updateFullName,
            UpdateProfileType.EMAIL, this::updateEmail,
            UpdateProfileType.PHONE, this::updatePhone
    );


    public ProfileService(
            UserRepository userRepository,
            UploadHandler uploadHandler,
            ApplicationEventPublisher eventPublisher,
            PasswordEncoder passwordEncoder,
            UpdateProfileCacheService updateProfileCacheService) {

        this.userRepository = userRepository;
        this.uploadHandler = uploadHandler;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.updateProfileCacheService = updateProfileCacheService;
    }

    @Cacheable(value = "profile", key = "{#userId}")
    public ApiResponse<ProfileDTO> getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDTO profile = UserMapper.userToUserDTO(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "Get profile successfully", profile);
    }

    /* .
    public ApiResponse<String> updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getConfirmPassword(), user.getPassword())) {
            throw new AuthenticationException("Password does not match") {
            };
        }

        switch (request.getType()) {
            case FULL_NAME -> {
                user.setFullName(request.getNewInfo());
                userRepository.save(user);
                return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        "정보 변경이 되었습니다.");
            }

            case EMAIL -> {
                if (HelperUtils.isInvalidEmail(request.getNewInfo())) {
                    throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다");
                }
                String newEmail = HelperUtils.normalizeEmail(request.getNewInfo());

                if (user.getEmail().equals(newEmail)) {
                    throw new BadRequestException("입력한 이메일 주소는 본 계정의 이메일입니다");
                }
                if (userRepository.existsByEmail(newEmail)) {
                    throw new AlreadyExistsException("이미 등록된 이메일입니다.");
                }
                return cacheEmail(userId, newEmail, user.getFullName());
            }
            case PHONE -> {
                user.setPhone(request.getNewInfo());
                return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        "phone 변경이 되었습니다.");
            }
            default -> throw new IllegalArgumentException("Unsupported update type");
        }
    }
     */
    @CacheEvict(value = "profile", allEntries = true)
    public ApiResponse<String> updateProfile(Long userId, UpdateProfileRequest request) {
        if (updateProfileCacheService.isBlocked(userId)) {
            throw new LockedException("비밀번호를 5번 이상 입력 잘못했습니다. 잠시 후 시도하세요.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getConfirmPassword(), user.getPassword())) {
            updateProfileCacheService.verifyPasswordFail(userId);
            throw new BadCredentialsException("비밀번호가 틀렸습니다");
        }

        updateProfileCacheService.verifyPasswordPassed(userId);
        return handlerMap.getOrDefault(request.getType(), this::unsupported).apply(user, request.getNewInfo());
    }

    private ApiResponse<String> updateFullName(User user, String newName) {
        String norNewName = HelperUtils.normalizeUsername(newName);

        if (user.getFullName().equals(norNewName)) {
            throw new BadRequestException("현재 본 계정 이릅이니다.");
        }
        user.setFullName(norNewName);
        userRepository.save(user);
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정보 변경이 되었습니다.");
    }

    private ApiResponse<String> updateEmail(User user, String newEmail) {
        if (HelperUtils.isInvalidEmail(newEmail)) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다");
        }
        String normalizeEmail = HelperUtils.normalizeEmail(newEmail);

        if (user.getEmail().equals(normalizeEmail)) {
            throw new BadRequestException("입력한 이메일 주소는 본 계정의 이메일입니다");
        }
        if (userRepository.existsByEmail(normalizeEmail)) {
            throw new AlreadyExistsException("이미 등록된 이메일입니다.");
        }
        return cacheEmail(user.getId(), newEmail, user.getFullName());
    }

    private ApiResponse<String> cacheEmail(Long userId, String newEmail, String currentFullName) {
        if (updateProfileCacheService.existEmailCache(userId)) {
            String token = updateProfileCacheService.getTokenCache(userId);
            PendingEmailUpdate pendingEmailCache = updateProfileCacheService.getPendingEmailCache(token);

            log.info("Cache Pending Email: {}", pendingEmailCache);

            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "이메일 변경 요청하셨습니다! 이메일 다시 확인하세요");
        }
        String token = UUID.randomUUID().toString();
        PendingEmailUpdate pendingEmail = new PendingEmailUpdate(userId, newEmail);
        updateProfileCacheService.saveEmailCache(
                token,
                pendingEmail);

        updateProfileCacheService.saveCacheLock(userId, token);

        log.info("New Pending Email: {}", pendingEmail);

        eventPublisher.publishEvent(new UpdateProfileEvent(currentFullName, newEmail, token)); //send email
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "이메일 주소 변경을 인증하기 위한 이메일을 보냈습니다. 이메일을 확인하세요");
    }

    //개인 프로젝트이기 때문에 휴대폰 변경이 간단하게 구현함. 실제는 이메일처럼 구현.
    private ApiResponse<String> updatePhone(User user, String newPhone) {
        if (HelperUtils.isInvalidPhone(newPhone)) {
            throw new BadRequestException("휴대폰 형식이 올바르지 않습니다");
        }

        if (user.getPhone().equals(newPhone)) {
            throw new BadRequestException("입력한 휴대폰는 본 계정의 휴대폰입니다");
        }

        if (userRepository.existsByPhone(newPhone)) {
            throw new AlreadyExistsException("이미 등록된 휴대톤입니다.");
        }

        user.setPhone(newPhone);
        userRepository.save(user);
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "휴대폰 변경이 되었습니다.");
    }

    private ApiResponse<String> unsupported(User user, String keyword) {
        throw new BadRequestException("지원하지 않는 타입입니다");
    }

    @Transactional
    @CacheEvict(value = "profile", allEntries = true)
    public ApiResponse<String> verifyUpdateEmailByToken(String token) {
        PendingEmailUpdate pendingEmailUpdate = updateProfileCacheService.getPendingEmailCache(token);
        if (pendingEmailUpdate == null) {
            throw new BadRequestException("Token 유효 기간이 지났습니다.");
        }

        User user = userRepository.findById(pendingEmailUpdate.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setEmail(pendingEmailUpdate.newEmail());

        userRepository.save(user);
        updateProfileCacheService.invalidateEmailCache(token);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "이메일 업데이트가 와료되었습니다.");
    }

    @CacheEvict(value = "profile", allEntries = true)
    public ApiResponse<String> uploadAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String avatarUrl = uploadHandler.uploadFile(userId, file);

        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), avatarUrl);
    }
}
