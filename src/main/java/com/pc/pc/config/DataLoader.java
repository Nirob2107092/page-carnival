package com.pc.pc.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pc.pc.model.Role;
import com.pc.pc.model.RoleType;
import com.pc.pc.model.User;
import com.pc.pc.repository.RoleRepository;
import com.pc.pc.repository.UserRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleType.ADMIN));
            roleRepository.save(new Role(RoleType.SELLER));
            roleRepository.save(new Role(RoleType.BUYER));
        }

        if (userRepository.findByEmail("admin@pagecarnival.com").isEmpty()) {
            Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = new User();
            admin.setFullName("Admin");
            admin.setEmail("admin@pagecarnival.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRole(adminRole);

            userRepository.save(admin);
        }
    }
}
