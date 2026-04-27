package com.catholic.ac.kr.booking_platform.security;

import com.catholic.ac.kr.booking_platform.model.User;
import com.catholic.ac.kr.booking_platform.repository.UserRepository;
import com.catholic.ac.kr.booking_platform.security.custom.CustomAuthenticationEntryPoint;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final String REACT_PORT;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(UserRepository userRepository, @Value("${react.port}") String REACT_PORT, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.userRepository = userRepository;
        this.REACT_PORT = REACT_PORT;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("user not found"));

            if (!user.isEnabled()) {
                throw new DisabledException("활성화되지 않음");
            }
            return new UserDetailsImpl(user);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService());

        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository repo) {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of(REACT_PORT));
                    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // save token vào Cookie để Frontend đọc được
                        .ignoringRequestMatchers("/auth/**", "/graphql", "/graphiql/**")
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                )
                .sessionManagement(s -> {
                    s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    s.maximumSessions(1).sessionRegistry(sessionRegistry());
                })
                .exceptionHandling(exception -> exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .securityContext(context -> {
                    context.securityContextRepository(repo);
                    context.requireExplicitSave(true);
                })
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(AbstractHttpConfigurer::disable) //기존 form 로그인 사용하지 않음
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/health/**", "/actuator/**").permitAll()
                                .requestMatchers(
                                        "/ws/**",
                                        "/auth/**",
                                        "/*.html", "/*.css", "/*.js",
                                        "/*.png", "/*.jpg", "/*.svg",
                                        "/icon/**", "/media/**").permitAll()
                                .requestMatchers("/graphiql", "/graphiql/**").permitAll()
                                .requestMatchers("/graphql").permitAll() // GraphQL (dev)
                                .anyRequest()
                                .authenticated()
                );

        return http.build();
    }

}
