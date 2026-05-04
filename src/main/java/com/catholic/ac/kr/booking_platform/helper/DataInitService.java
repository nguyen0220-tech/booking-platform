//package com.catholic.ac.kr.booking_platform.helper;
//
//import com.catholic.ac.kr.booking_platform.user.constant.RoleName;
//import com.catholic.ac.kr.booking_platform.user.data.Role;
//import com.catholic.ac.kr.booking_platform.user.data.RoleRepository;
//import com.catholic.ac.kr.booking_platform.user.data.User;
//import com.catholic.ac.kr.booking_platform.user.data.UserRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class DataInitService {
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @PostConstruct
//    public void initAdmin() {
//        Role userRole = roleRepository.findByName(RoleName.USER)
//                .orElseGet(() -> {
//                    Role role = new Role();
//                    role.setName(RoleName.USER);
//                    return roleRepository.save(role);
//                });
//
//        Role providerRole = roleRepository.findByName(RoleName.PROVIDER)
//                .orElseGet(() -> {
//                    Role role = new Role();
//                    role.setName(RoleName.PROVIDER);
//                    return roleRepository.save(role);
//                });
//
//        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
//                .orElseGet(() -> {
//                    Role role = new Role();
//                    role.setName(RoleName.ADMIN);
//                    return roleRepository.save(role);
//                });
//
//
//        User admin = createBaseUser(Set.of(adminRole, userRole), 1);
//        admin.setUsername("admin");
//        admin.setEmail("test@gmail.com");
//        admin.setPhone("123456789");
//        admin.setFullName("관리자");
//
//        userRepository.save(admin);
//
//        User provider = createBaseUser(Set.of(providerRole), 1);
//        provider.setUsername("provider");
//        provider.setEmail("test111@gmail.com");
//        provider.setPhone("1234567890");
//        provider.setFullName("제공자");
//
//        userRepository.save(provider);
//    }
//
//    @PostConstruct
//    public void initUserTest() {
//        Role userRole = roleRepository.findByName(RoleName.USER)
//                .orElseGet(() -> {
//                    Role role = new Role();
//                    role.setName(RoleName.USER);
//                    return roleRepository.save(role);
//                });
//
//        for (int i = 0; i < 10; i++) {
//            User user = createBaseUser(Set.of(userRole), i);
//
//            user.setUsername("user__" + i);
//            user.setEmail("test__" + i + "@gmail.com");
//            user.setPhone("100000001" + i);
//            user.setFullName("user test_ " + i);
//            userRepository.save(user);
//
//        }
//    }
//
//    private User createBaseUser(Set<Role> roles, int num) {
//        User user = new User();
//
//        user.setPassword(passwordEncoder.encode("123456"));
//        user.setEnabled(true);
//        user.setAvatarUrl(null);
//        user.setRoles(roles);
//        user.setCreatedAt(LocalDateTime.now());
//
//        user.setBlocked(num % 2 == 0);
//
//        return user;
//    }
//}
