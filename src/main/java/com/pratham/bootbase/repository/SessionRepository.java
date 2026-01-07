package com.pratham.bootbase.repository;

import com.pratham.bootbase.entity.AppUser;
import com.pratham.bootbase.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    List<Session> findByAppUserOrderByLastUsedAtAsc(AppUser appUser);

    Optional<Session> findByAppUserIdAndRefreshToken(Long userId, String refreshToken);

    @Modifying
    void deleteByAppUserIdAndRefreshToken(Long userId, String refreshToken);
}
