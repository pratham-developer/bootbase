package com.pratham.bootbase.service;

import com.pratham.bootbase.entity.AppUser;
import com.pratham.bootbase.entity.Session;
import com.pratham.bootbase.exception.SessionNotFoundException;
import com.pratham.bootbase.repository.SessionRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final JwtService jwtService;
    private final EntityManager entityManager;

    @Transactional
    public void createSession(AppUser appUser, String refreshToken){
        int allowedSessions = appUser.getSessionLimit();
        List<Session> existingSessions = sessionRepository.findByAppUserOrderByLastUsedAtAsc(appUser);
        if(existingSessions.size()==allowedSessions){
            Session toDelete = existingSessions.getFirst();
            sessionRepository.delete(toDelete);
        }
        Session toCreate = Session.builder()
                .appUser(appUser)
                .refreshToken(refreshToken)
                .lastUsedAt(LocalDateTime.now())
                .build();
        sessionRepository.save(toCreate);
    }

    @Transactional
    public String validateAndUpdateSession(Long userId, String refreshToken){
        Session session = sessionRepository.findByAppUserIdAndRefreshToken(userId,refreshToken)
                .orElseThrow(
                        ()->new SessionNotFoundException("No Existing Session Found. Please login.")
                );
        String newRefreshToken = jwtService.refreshTokenBuilder(userId);
        session.setLastUsedAt(LocalDateTime.now());
        session.setRefreshToken(newRefreshToken);
        sessionRepository.save(session);
        return newRefreshToken;
    }
}
