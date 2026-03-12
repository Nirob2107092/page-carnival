package com.pc.pc.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.pc.model.Role;
import com.pc.pc.model.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}