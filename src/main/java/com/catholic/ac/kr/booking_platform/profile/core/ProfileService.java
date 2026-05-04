package com.catholic.ac.kr.booking_platform.profile.core;

import com.catholic.ac.kr.booking_platform.helper.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.infrastructure.components.UploadHandler;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.infrastructure.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.profile.core.strategy.ProfileStrategy;
import com.catholic.ac.kr.booking_platform.profile.data.PendingEmailUpdate;
import com.catholic.ac.kr.booking_platform.profile.data.ProfileDTO;
import com.catholic.ac.kr.booking_platform.profile.data.UpdateProfileRequest;
import com.catholic.ac.kr.booking_platform.user.UserMapper;
import com.catholic.ac.kr.booking_platform.user.data.User;
import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final UploadHandler uploadHandler;
    private final PasswordEncoder passwordEncoder;
    private final UpdateProfileCacheService updateProfileCacheService;
    private final Map<UpdateProfileType, ProfileStrategy> profileStrategyMap;


    public ProfileService(
            UserRepository userRepository,
            UploadHandler uploadHandler,
            PasswordEncoder passwordEncoder,
            UpdateProfileCacheService updateProfileCacheService,
            List<ProfileStrategy> profileStrategyList) {

        this.userRepository = userRepository;
        this.uploadHandler = uploadHandler;
        this.passwordEncoder = passwordEncoder;
        this.updateProfileCacheService = updateProfileCacheService;
        this.profileStrategyMap = profileStrategyList.stream()
                .collect(Collectors.toMap(ProfileStrategy::getUpdateProfileType, p -> p));
    }

    @Cacheable(value = "profile", key = "{#userId}")
    public ApiResponse<ProfileDTO> getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ProfileDTO profile = UserMapper.userToUserDTO(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "Get profile successfully", profile);
    }

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

        ProfileStrategy strategy = profileStrategyMap.get(request.getType());

        return strategy.updateProfile(user, request);
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
