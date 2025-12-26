package com.pratham.bootbase.repository;

import com.pratham.bootbase.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    Boolean existsByEmail(String email);
}
