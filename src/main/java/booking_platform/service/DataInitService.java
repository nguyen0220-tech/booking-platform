package booking_platform.service;

import booking_platform.enumdef.RoleName;
import booking_platform.model.Role;
import booking_platform.model.User;
import booking_platform.repository.RoleRepository;
import booking_platform.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initAdmin() {
        roleRepository.findByName(RoleName.USER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.USER);
                    return roleRepository.save(role);
                });

        roleRepository.findByName(RoleName.PROVIDER)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.PROVIDER);
                    return roleRepository.save(role);
                });

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(RoleName.ADMIN);
                    return roleRepository.save(role);
                });


        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setEmail("test@gmail.com");
        admin.setPhone("123456789");
        admin.setFullName("관리자");
        admin.setEnabled(true);
        admin.setAvatarUrl(null);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}
