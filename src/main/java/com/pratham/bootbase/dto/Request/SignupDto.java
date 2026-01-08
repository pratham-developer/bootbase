package com.pratham.bootbase.dto.Request;

import com.pratham.bootbase.entity.enums.Role;
import com.pratham.bootbase.entity.enums.Subscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private String name;
    private String email;
    private String password;
    private Set<Role> roles;
    private Subscription subscription;
    private Integer sessionLimit;

}
