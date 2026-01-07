package com.pratham.bootbase.handlers;

import com.pratham.bootbase.entity.AppUser;
import com.pratham.bootbase.service.AppUserService;
import com.pratham.bootbase.service.JwtService;
import com.pratham.bootbase.service.SessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppUserService appUserService;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        //get the user with this email or create
        AppUser appUser = appUserService.findOrCreateByEmail(email,name);

        //generate access and refresh token for this user
        String accessToken = jwtService.generateAccessToken(appUser);
        String refreshToken = jwtService.generateRefreshToken(appUser);

        //create a session for this user with this refresh token
        sessionService.createSession(appUser,refreshToken);

        //send refresh token in cookie
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(deployEnv.equals("production")); //secure only in prod
        cookie.setPath("/api/v1/auth/refresh"); //strictly restrict to the refresh endpoint
        cookie.setAttribute("SameSite", "Strict"); //prevent csrf
        response.addCookie(cookie);

        String frontendUrl = "http://localhost:8080/api/v1/home.html?accessToken="+accessToken;

        response.sendRedirect(frontendUrl);


    }
}
