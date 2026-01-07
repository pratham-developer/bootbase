package com.pratham.bootbase.auth;

import com.pratham.bootbase.dto.AuthenticatedUser;
import com.pratham.bootbase.entity.AppUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof AppUser){
            return Optional.of(((AppUser) principal).getUsername());
        }
        if(principal instanceof AuthenticatedUser){
            return Optional.of(((AuthenticatedUser) principal).getUsername());
        }
        return Optional.empty();
    }
}
