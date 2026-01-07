package com.pratham.bootbase.service;

import com.pratham.bootbase.entity.AppUser;
import com.pratham.bootbase.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username).orElseThrow(
                ()->new UsernameNotFoundException("User not found with email = "+username)
        );
    }

    public AppUser loadUserById(Long userId) throws BadCredentialsException {
        return appUserRepository.findById(userId).orElseThrow(
                ()->new BadCredentialsException("User not found with id = "+userId)
        );
    }

    @Transactional
    public AppUser findOrCreateByEmail(String email, String name){
        Optional<AppUser> appUser = appUserRepository.findByEmail(email);
        if(appUser.isPresent()) return appUser.get();

        AppUser toSave = AppUser.builder()
                .email(email)
                .name(name)
                .build();

        return appUserRepository.save(toSave);
    }
}
