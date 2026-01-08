package com.pratham.bootbase.filters;

import com.pratham.bootbase.dto.AccessTokenClaims;
import com.pratham.bootbase.dto.AuthenticatedUser;
import com.pratham.bootbase.entity.enums.Role;
import com.pratham.bootbase.entity.enums.Subscription;
import com.pratham.bootbase.exception.SessionNotFoundException;
import com.pratham.bootbase.service.JwtService;
import com.pratham.bootbase.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        try {
            String accessToken = authHeader.substring(7);

            if(SecurityContextHolder.getContext().getAuthentication() == null){

                AccessTokenClaims claims = jwtService.getClaimsFromAccessToken(accessToken);

                //check if token is blacklisted or not
                if(tokenBlacklistService.isAccessTokenBlacklisted(claims.getJti())){
                    throw new SessionNotFoundException("Session Expired");
                }

                Set<Role> roles = claims.getRoles().stream()
                        .map(role -> Role.valueOf(role))
                        .collect(Collectors.toSet());

                AuthenticatedUser authenticatedUser = AuthenticatedUser
                        .builder().id(claims.getId()).name(claims.getName())
                        .email(claims.getEmail()).roles(roles).subscription(Subscription.valueOf(claims.getSubscription()))
                        .build();


                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request,response,null,e);
            return;
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth/");
    }
}
