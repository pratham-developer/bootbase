package com.pratham.bootbase.service;

import com.pratham.bootbase.entity.AppUser;
import com.pratham.bootbase.entity.Session;
import com.pratham.bootbase.exception.SessionNotFoundException;
import com.pratham.bootbase.repository.SessionRepository;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    private final int allowedSessions = 2;
    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void createSession(AppUser appUser, String refreshToken){
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
    public void validateAndUpdateSession(Long userId, String refreshToken){
        Session session = sessionRepository.findByAppUserIdAndRefreshToken(userId,refreshToken)
                .orElseThrow(
                        ()->new SessionNotFoundException("No Existing Session Found. Please login.")
                );

        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
