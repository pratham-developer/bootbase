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
import org.springframework.http.ResponseCookie;
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
        cookie.setPath("/api/v1/auth"); //strictly restrict to the refresh endpoint
        cookie.setAttribute("SameSite", "Strict"); //prevent csrf

        response.addCookie(cookie);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshCookie,
            @RequestHeader(name = "X-Refresh-Token", required = false) String refreshHeader,
            HttpServletResponse response){

        String refreshToken = refreshCookie != null ? refreshCookie : refreshHeader;
        LoginResponseDto responseDto = authService.refresh(refreshToken);

        //send refresh token in cookie
        Cookie cookie = new Cookie("refreshToken",responseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(deployEnv.equals("production")); //secure only in prod
        cookie.setPath("/api/v1/auth"); //strictly restrict to the refresh endpoint
        cookie.setAttribute("SameSite", "Strict"); //prevent csrf

        response.addCookie(cookie);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshCookie,
            @RequestHeader(name = "X-Refresh-Token", required = false) String refreshHeader,
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ){
        String refreshToken = refreshCookie != null ? refreshCookie : refreshHeader;
        String accessToken = null;
        if(authHeader!=null && authHeader.startsWith("Bearer ")){
            accessToken = authHeader.substring(7);
        }
        //logout
        authService.logout(refreshToken, accessToken);
        //delete refresh cookie
        ResponseCookie deleteCookie = deleteRefreshCookie();
        return ResponseEntity.noContent()
                .header("Set-Cookie",deleteCookie.toString())
                .build();
    }

    private ResponseCookie deleteRefreshCookie(){
        return ResponseCookie.from("refreshToken","")
                .httpOnly(true)
                .secure(deployEnv.equals("production"))
                .sameSite("Strict")
                .path("/api/v1/auth")
                .maxAge(0)
                .build();
    }

}
