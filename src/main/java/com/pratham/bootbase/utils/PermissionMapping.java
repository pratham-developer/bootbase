package com.pratham.bootbase.utils;

import com.pratham.bootbase.entity.enums.Permission;
import com.pratham.bootbase.entity.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.pratham.bootbase.entity.enums.Permission.*;
import static com.pratham.bootbase.entity.enums.Role.*;

public class PermissionMapping {

    private static final Map<Role, Set<Permission>> permissionMap = Map.of(
            USER, Set.of(READ_EMPLOYEE),
            MANAGER, Set.of(READ_EMPLOYEE, MODIFY_EMPLOYEE),
            ADMIN, Set.of(READ_EMPLOYEE,MODIFY_EMPLOYEE,DELETE_EMPLOYEE)
    );

    public static Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        roles.forEach(role -> {
            //add role as authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

            // Add permissions as authority
            permissionMap.getOrDefault(role, Set.of())
                    .forEach(permission ->
                            authorities.add(new SimpleGrantedAuthority(permission.name()))
                    );
        });

        return authorities;
    }



}
