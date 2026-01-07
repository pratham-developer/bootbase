package com.pratham.bootbase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenClaims {
    private String jti;
    private Long id;
    private String name;
    private String email;
    private Long expirationTime;
    private List<String> roles;
}
