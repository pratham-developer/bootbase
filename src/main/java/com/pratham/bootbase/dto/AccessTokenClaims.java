package com.pratham.bootbase.dto;

import com.pratham.bootbase.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenClaims {
    private Long id;
    private String name;
    private String email;
    private List<String> roles;
}
