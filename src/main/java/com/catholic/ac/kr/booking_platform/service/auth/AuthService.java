package com.catholic.ac.kr.booking_platform.service.auth;

import com.catholic.ac.kr.booking_platform.dto.request.*;
import com.catholic.ac.kr.booking_platform.dto.response.ApiResponse;
import com.catholic.ac.kr.booking_platform.dto.response.LoginResponse;
import com.catholic.ac.kr.booking_platform.enumdef.CheckType;
import com.catholic.ac.kr.booking_platform.enumdef.TokenType;
import com.catholic.ac.kr.booking_platform.event.ForgotPasswordEvent;
import com.catholic.ac.kr.booking_platform.event.RegistrySuccessEvent;
import com.catholic.ac.kr.booking_platform.exception.AlreadyExistsException;
import com.catholic.ac.kr.booking_platform.exception.BadRequestException;
import com.catholic.ac.kr.booking_platform.exception.ResourceNotFoundException;
import com.catholic.ac.kr.booking_platform.helper.HelperUtils;
import com.catholic.ac.kr.booking_platform.mapper.ResponserMapper;
import com.catholic.ac.kr.booking_platform.model.Role;
import com.catholic.ac.kr.booking_platform.model.TokenVerify;
import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.RoleRepository;
import com.catholic.ac.kr.booking_platform.repository.TokenVerifyRepository;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import com.catholic.ac.kr.booking_platform.service.TokenVerifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
    private final SessionRegistry sessionRegistry;


    public AuthService(
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            LoginAttemptService loginAttemptService, ApplicationEventPublisher eventPublisher,
            PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository,
            TokenVerifyService tokenVerifyService, TokenVerifyRepository tokenVerifyRepository, SessionRegistry sessionRegistry) {

        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.loginAttemptService = loginAttemptService;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.tokenVerifyService = tokenVerifyService;
        this.tokenVerifyRepository = tokenVerifyRepository;
        this.sessionRegistry = sessionRegistry;
    }

    public ApiResponse<String> registry(RegistryRequest request) {
        String username = HelperUtils.normalizeUsername(request.getUsername());
        String password = HelperUtils.normalizePassword(request.getPassword());
        String email = HelperUtils.normalizeEmail(request.getEmail());
        String phone = HelperUtils.normalizePhone(request.getPhone());
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!user.isEnabled()) {
                setBaseInfoUser(user, username, password, request.getFullName(), email, phone, role);

                userRepository.save(user);
                generateTokenAndSendEmail(user);

                return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        "이미 등록된 이메일입니다. 최신 정보로 갱신되었으며 인증 이메일을 다시 발송했습니다.");
            } else {
                throw new AlreadyExistsException("사용중인 이메일입니다.아이디나 비밀번호를 잊으신 경우 ID/PW 찾기 기능을 이용하세요. ");
            }
        }
        User newUser = new User();

        setBaseInfoUser(newUser, username, password, request.getFullName(), email, phone, role);

        newUser.setEnabled(false);
        newUser.setBlocked(false);
        newUser.setRoles(Set.of(roleRepository
                .findByName(request.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"))));

        userRepository.save(newUser);
        generateTokenAndSendEmail(newUser);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "회원 가입해 주셔 감사합니다. 이메일 (" + HelperUtils.encodeEmail(request.getEmail()) + ")을 확인해 주세요");
    }

    private void setBaseInfoUser(User user, String username, String password,
                                 String fullName, String email, String phone, Role role) {

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
        user.setCreatedAt(LocalDateTime.now());
    }

    private void generateTokenAndSendEmail(User user) {
        String token = tokenVerifyService.createToken(user, TokenType.VERIFY_REGISTRY);
        eventPublisher.publishEvent(
                new RegistrySuccessEvent(user.getUsername(), user.getEmail(), user.getPhone(), token));
    }

    @Transactional(noRollbackFor = BadRequestException.class)
    public ApiResponse<String> verifyNewUser(String token) {
        TokenVerify tokenVerify = tokenVerifyRepository.findByTokenAndType(token, TokenType.VERIFY_REGISTRY);

        if (tokenVerify == null || tokenVerify.getExpiryDate().isBefore(LocalDateTime.now())) {
            if (tokenVerify != null) {
                refreshTokenVerifyAndSend(tokenVerify);
                throw new BadRequestException("인증 기간이 만료되었습니다. 새로 에메일이 발송됩니다.");
            }
            throw new BadRequestException("인증 실패");
        }

        User user = tokenVerify.getUser();
        if (user.isEnabled()) {
            return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                    "이미 확정된 계정입니다.");
        }

        user.setEnabled(true);
        userRepository.save(user);

        tokenVerifyRepository.deleteByIdCustom(tokenVerify.getId());

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), "새로운 계정이 정상적으로 확정되었습니다");
    }

    private void refreshTokenVerifyAndSend(TokenVerify tokenVerify) {
        tokenVerify.setToken(UUID.randomUUID().toString());
        tokenVerify.setCreated(LocalDateTime.now());
        tokenVerify.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenVerifyRepository.save(tokenVerify);

        User user = tokenVerify.getUser();
        eventPublisher.publishEvent(
                new RegistrySuccessEvent(user.getUsername(), user.getEmail(), user.getPhone(), tokenVerify.getToken())
        );
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

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        HttpSession session = httpRequest.getSession(true);
        sessionRegistry.registerNewSession(session.getId(), Objects.requireNonNull(userDetails));

        LoginResponse response = ResponserMapper.toLoginResponse(userDetails);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "Login Successful", response);
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
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
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

    public ApiResponse<String> forgotUsername(ForgotUsernameRequest request) {
        String norEmail = HelperUtils.normalizeEmail(request.getEmail());
        User user = userRepository.findByEmail(norEmail)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾지 못했습니다"));

        if (!user.getPhone().equals(request.getPhone())) {
            throw new BadRequestException("입력하신 정보가 일치하지 않습니다.");
        }
        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "정상적으로 처리되었습니다", user.getUsername());

    }

    public ApiResponse<String> forgotPassword(ForgotPasswordRequest request) {
        String nolUsername = HelperUtils.normalizeUsername(request.getUsername());
        User user = userRepository.findByUsername(nolUsername)
                .orElseThrow(() -> new ResourceNotFoundException("사용자가 존재하면 이메일로 안내를 보내드립니다."));

        if (user.isBlocked() || !user.isEnabled()) {
            throw new LockedException("본 계정은 비정상적인상태입니다");
        }

        String token = tokenVerifyService.createToken(user, TokenType.VERIFY_FORGOT_PASSWORD);

        eventPublisher.publishEvent(new ForgotPasswordEvent(user.getFullName(), user.getEmail(), token));

        String emailEncoded = HelperUtils.encodeEmail(user.getEmail());

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "비밀번호 변경릏 인증하기 위해 이메일 " + emailEncoded + "으로 보냈습니다");
    }

    @Transactional
    public ApiResponse<String> resetPassword(NewPasswordRequest request) {
        TokenVerify tokenVerify = tokenVerifyRepository
                .findByTokenAndType(request.getToken(), TokenType.VERIFY_FORGOT_PASSWORD);

        if (tokenVerify == null || tokenVerify.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("인증 기간이 만료되었습니다");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("입력하신 비밀번호가 확인되지 않았습니다.");
        }

        User user = tokenVerify.getUser();

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        tokenVerifyRepository.delete(tokenVerify);

        userRepository.save(user);

        return ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                "비밀번호가 새로 변경되었습니다");
    }

}
