package com.pratham.bootbase.controller;

import com.pratham.bootbase.dto.Request.LoginDto;
import com.pratham.bootbase.dto.Request.SignupDto;
import com.pratham.bootbase.dto.Response.AppUserDto;
import com.pratham.bootbase.dto.Response.LoginResponseDto;
import com.pratham.bootbase.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${deploy.env}")
    private String deployEnv;

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AppUserDto> signup(@RequestBody SignupDto signupDto){
        return ResponseEntity.ok(authService.signup(signupDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response){
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        //send refresh token in cookie
        Cookie cookie = new Cookie("refreshToken",loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(deployEnv.equals("production")); //secure only in prod
        cookie.setPath("/auth/refresh"); //strictly restrict to the refresh endpoint
        cookie.setAttribute("SameSite", "Strict"); //prevent csrf

        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @CookieValue(name = "refreshToken", required = false) String cookieToken,
            @RequestHeader(name = "X-Refresh-Token", required = false) String headerToken
    ){
        String refreshToken = cookieToken != null ? cookieToken : headerToken;
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

}
