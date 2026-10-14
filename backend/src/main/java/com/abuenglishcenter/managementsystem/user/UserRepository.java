package com.abuenglishcenter.managementsystem.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByRole(Role role);
    Optional<User> findByUsername(String username);
}
