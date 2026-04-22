//package com.catholic.ac.kr.booking_platform.service;
//
//import com.catholic.ac.kr.booking_platform.enumdef.RoleName;
//import com.catholic.ac.kr.booking_platform.model.Role;
//import com.catholic.ac.kr.booking_platform.model.User;
//import com.catholic.ac.kr.booking_platform.repository.RoleRepository;
//import com.catholic.ac.kr.booking_platform.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
//@Component
//public class DataInitService {
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public DataInitService(
//            UserRepository userRepository,
//            RoleRepository roleRepository,
//            PasswordEncoder passwordEncoder) {
//
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
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
//        User admin = createBaseUser(Set.of(adminRole, userRole, providerRole), 1);
//        admin.setUsername("admin");
//        admin.setEmail("test@gmail.com");
//        admin.setPhone("123456789");
//        admin.setFullName("관리자");
//
//        userRepository.save(admin);
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
//        for (int i = 0; i < 50; i++) {
//            User user = createBaseUser(Set.of(userRole), i);
//
//            user.setUsername("user_" + i);
//            user.setEmail("test_" + i + "@gmail.com");
//            user.setPhone("10000000" + i);
//            user.setFullName("user test " + i);
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
//
//        user.setBlocked(num % 2 == 0);
//
//        return user;
//    }
//}
