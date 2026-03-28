package booking_platform.security;

import booking_platform.model.User;
import booking_platform.repository.UserRepository;
import booking_platform.security.userdetails.UserDetailsImpl;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
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

            System.out.println("Query db:" + user.getFullName());

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository repo) {
        http.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // save token vào Cookie để Frontend đọc được
                        .ignoringRequestMatchers("/auth/**", "/graphql", "/graphiql/**")
                )
                .sessionManagement(s -> {
                    s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
                    s.maximumSessions(1);
                })
                .securityContext(context -> {
                    context.securityContextRepository(repo);
                    context.requireExplicitSave(true);
                })
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(AbstractHttpConfigurer::disable) //기존 form 로그인 사용하지 않음
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/health").permitAll()
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
