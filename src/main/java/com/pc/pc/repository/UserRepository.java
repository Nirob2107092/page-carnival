package com.pc.pc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.pc.model.RoleType;
import com.pc.pc.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole_Name(RoleType roleType);
    long countByRole_Name(RoleType roleType);
}