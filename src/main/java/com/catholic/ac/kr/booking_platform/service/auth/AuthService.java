package com.catholic.ac.kr.booking_platform.service.auth;

import com.catholic.ac.kr.booking_platform.dto.request.CheckInfoRequest;
import com.catholic.ac.kr.booking_platform.dto.request.LoginRequest;
import com.catholic.ac.kr.booking_platform.dto.request.RegistryRequest;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.dto.response.LoginResponse;
import com.catholic.ac.kr.booking_platform.enumdef.CheckType;
import com.catholic.ac.kr.booking_platform.event.RegistrySuccessEvent;
import com.catholic.ac.kr.booking_platform.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.mapper.ResponserMapper;
import com.catholic.ac.kr.booking_platform.model.TokenVerify;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.RoleRepository;
import com.catholic.ac.kr.booking_platform.repository.TokenVerifyRepository;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.service.TokenVerifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final LoginAttemptService loginAttemptService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenVerifyService tokenVerifyService;
    private final TokenVerifyRepository tokenVerifyRepository;

    private final Map<CheckType, Function<String, ApiResponse<String>>> handlerMap = Map.of(
            CheckType.USERNAME, this::checkUserName,
            CheckType.EMAIL, this::checkEmail,
            CheckType.PHONE, this::checkPhone);


    public AuthService(
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            LoginAttemptService loginAttemptService, ApplicationEventPublisher eventPublisher,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository,
            TokenVerifyService tokenVerifyService, TokenVerifyRepository tokenVerifyRepository) {

        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.loginAttemptService = loginAttemptService;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.tokenVerifyService = tokenVerifyService;
        this.tokenVerifyRepository = tokenVerifyRepository;
    }

    public ApiResponse<String> registry(RegistryRequest request) {
        User newUser = new User();

        String username = HelperUtils.normalizeUsername(request.getUsername());
        String password = HelperUtils.normalizePassword(request.getPassword());
        String email = HelperUtils.normalizeEmail(request.getEmail());
        String phone = HelperUtils.normalizePhone(request.getPhone());

        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFullName(request.getFullName());
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setEnabled(false);
        newUser.setBlocked(false);
        newUser.setRoles(Set.of(roleRepository.findByName(request.getRole()).orElseThrow(() -> new ResourceNotFoundException("Role not found"))));

        userRepository.save(newUser);

        String token = tokenVerifyService.createToken(newUser);

        eventPublisher.publishEvent(new RegistrySuccessEvent(request.getUsername(), request.getEmail(), request.getPhone(), token));

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "회원 가입해 주셔 감사합니다. 이메일 (" + HelperUtils.encodeEmail(request.getEmail()) + ")을 확인해 주세요");
    }

    @Transactional
    public ApiResponse<String> verifyNewUser(String token) {
        TokenVerify tokenVerify = tokenVerifyRepository.findByToken(token);

        if (tokenVerify == null || tokenVerify.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("확정 기간이 지났습니다");
        }

        User user = tokenVerify.getUser();

        if (user.isEnabled()) {
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "이미 확정된 계정입니다.");
        }

        user.setEnabled(true);

        userRepository.save(user);

        tokenVerifyRepository.deleteByIdCustom(tokenVerify.getId());

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "새로운 계정이 정상적으로 확정되었습니다");
    }


    public ApiResponse<LoginResponse> login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        if (loginAttemptService.isBlocked(request.getUsername())) {
            return ApiResponse.fail(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), "계정이 잠금 상태입니다. 10분 후에 로그인해 주세요.");
        }

        String username = HelperUtils.normalizeUsername(request.getUsername());
        String password = HelperUtils.normalizePassword(request.getPassword());

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        Authentication auth = authenticationManager.authenticate(token);

        /*
            authenticate()
            AuthenticationManager -> DaoAuthenticationProvider ->  UserDetailsService.loadUserByUsername()
             -> query DB -> PasswordEncoder.matches() -> return Authentication
         */
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

            /*.
            HttpSession session = httpRequest.getSession(true);

            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );
             */

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        LoginResponse response = ResponserMapper.toLoginResponse(userDetails);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "Login Successful", response);
    }

    public ApiResponse<Void> logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(httpRequest, httpResponse, auth);
        }
        /*
        SecurityContextHolder.clearContext()
        session.invalidate()
         */
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "로그아웃 성공");
    }

    public ApiResponse<String> checkExistInfo(CheckInfoRequest request) {
        String keyword = request.getKeyword().trim();
        return handlerMap.getOrDefault(request.getCheckType(), this::unsupported).apply(keyword);
    }

    private ApiResponse<String> checkUserName(String username) {
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            throw new BadRequestException("아이디 형식이 올바르지 않습니다");
        }
        return buildResponse(userRepository.existsByUsername(username), CheckType.USERNAME);
    }

    private ApiResponse<String> checkEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다");
        }

        return buildResponse(userRepository.existsByEmail(email), CheckType.EMAIL);
    }

    private ApiResponse<String> checkPhone(String phone) {
        if (!phone.matches("^\\d{9,11}$")) {
            throw new BadRequestException("전화번호 형식이 올바르지 않습니다");
        }

        return buildResponse(userRepository.existsByPhone(phone), CheckType.PHONE);
    }

    private ApiResponse<String> unsupported(String keyword) {
        throw new BadRequestException("지원하지 않는 타입입니다");
    }

    private ApiResponse<String> buildResponse(boolean exists, CheckType type) {
        String message = exists ? "사용중인 " + type.getMessage() + "입니다" : "사용가능한 " + type.getMessage() + "입니다";

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), message);
    }
}
