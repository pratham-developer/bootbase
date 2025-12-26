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
}
