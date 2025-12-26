package com.pratham.bootbase.controller;

import com.pratham.bootbase.dto.Request.LoginDto;
import com.pratham.bootbase.dto.Request.SignupDto;
import com.pratham.bootbase.dto.Response.AppUserDto;
import com.pratham.bootbase.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AppUserDto> signup(@RequestBody SignupDto signupDto){
        return ResponseEntity.ok(authService.signup(signupDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }

}
