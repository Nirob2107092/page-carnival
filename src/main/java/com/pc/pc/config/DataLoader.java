package com.pc.pc.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.pc.pc.model.Role;
import com.pc.pc.model.RoleType;
import com.pc.pc.repository.RoleRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(RoleType.ADMIN));
            roleRepository.save(new Role(RoleType.SELLER));
            roleRepository.save(new Role(RoleType.BUYER));
        }
    }
}