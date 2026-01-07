package com.pratham.bootbase.dto;

import com.pratham.bootbase.entity.enums.Role;
import com.pratham.bootbase.utils.PermissionMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticatedUser implements UserDetails {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return PermissionMapping.getAuthorities(roles);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
