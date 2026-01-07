package com.pratham.bootbase.entity;


import com.pratham.bootbase.entity.enums.Role;
import com.pratham.bootbase.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true,nullable = false)
    private String email;

    private String password; //password can be null for oauth user

    @Enumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //use permission mapping to get all the authorities
        return PermissionMapping.getAuthorities(roles);
    }

    @Override
    public String getUsername() {
        return email;
    }
}
