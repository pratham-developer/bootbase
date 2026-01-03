package com.pratham.bootbase.service;

import com.pratham.bootbase.dto.Request.LoginDto;
import com.pratham.bootbase.dto.Request.SignupDto;
import com.pratham.bootbase.dto.Response.AppUserDto;
import com.pratham.bootbase.dto.Response.LoginResponseDto;
import com.pratham.bootbase.entity.AppUser;
import com.pratham.bootbase.exception.BadRequestException;
import com.pratham.bootbase.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppUserService appUserService;
    private final SessionService sessionService;

    public AppUserDto signup(SignupDto signupDto){
        if(appUserRepository.existsByEmail(signupDto.getEmail())){
            throw new BadRequestException("Email is already being used");
        }
        AppUser user = modelMapper.map(signupDto, AppUser.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        AppUser savedUser = appUserRepository.save(user);
        return modelMapper.map(savedUser, AppUserDto.class);
    }

    public LoginResponseDto login(LoginDto loginDto){

        //auth manager will authenticate using the credentials
        //it sets the user inside the authentication object it returns
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        //get the user from the returned object
        AppUser authUser = (AppUser) auth.getPrincipal();

        //get the access token and refresh token for this authenticated user
        String accessToken = jwtService.generateAccessToken(authUser);
        String refreshToken = jwtService.generateRefreshToken(authUser);

        //create a session for this user with this refresh token
        sessionService.createSession(authUser,refreshToken);

        //return the tokens
        return LoginResponseDto.builder()
                .accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public LoginResponseDto refresh(String refreshToken) {
        //get the userId from refresh token
        Long userId = jwtService.getUserIdFromRefreshToken(refreshToken);

        //validate whether a session exists for this userId and refresh token
        sessionService.validateAndUpdateSession(userId, refreshToken);

        //get the authenticated user
        AppUser appUser = appUserService.loadUserById(userId);

        //generate new access token for this user
        String accessToken = jwtService.generateAccessToken(appUser);

        //send the response
        return LoginResponseDto.builder()
                .accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
