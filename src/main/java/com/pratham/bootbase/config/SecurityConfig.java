package com.pratham.bootbase.config;

import com.pratham.bootbase.filters.JwtAuthFilter;
import com.pratham.bootbase.handlers.Oauth2FailureHandler;
import com.pratham.bootbase.handlers.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final Oauth2FailureHandler oauth2FailureHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerExceptionResolver handlerExceptionResolver) throws Exception {
        httpSecurity
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**","/login.html","/home.html").permitAll() //public
                        /*
                        .requestMatchers(HttpMethod.GET,"/employees/**").hasAuthority(READ_EMPLOYEE.name())
                        .requestMatchers(HttpMethod.POST,"/employees/**").hasAuthority(MODIFY_EMPLOYEE.name())
                        .requestMatchers(HttpMethod.PATCH,"/employees/**").hasAuthority(MODIFY_EMPLOYEE.name())
                        .requestMatchers(HttpMethod.PUT,"/employees/**").hasAuthority(MODIFY_EMPLOYEE.name())
                        .requestMatchers(HttpMethod.DELETE,"/employees/**").hasAuthority(DELETE_EMPLOYEE.name())
                        */
                        .anyRequest().authenticated() //required authentication
                )
                .exceptionHandling(ex -> {
                    ex.authenticationEntryPoint((request, response, authException) ->
                            handlerExceptionResolver.resolveException(request,response,null,authException)
                    );
                    ex.accessDeniedHandler((request, response, accessDeniedException) ->
                            handlerExceptionResolver.resolveException(request,response,null,accessDeniedException)
                    );
                })
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .successHandler(oauth2SuccessHandler)
                        .failureHandler(oauth2FailureHandler)
                );

        return httpSecurity.build();
    }

}
