package com.pratham.bootbase.utils;

import com.pratham.bootbase.dto.AuthenticatedUser;
import com.pratham.bootbase.entity.AppUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long getCurrentUserId(){
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if(principal instanceof AppUser){
            return ((AppUser) principal).getId();
        }
        if(principal instanceof AuthenticatedUser){
            return ((AuthenticatedUser) principal).getId();
        }
        throw new IllegalStateException("Unknown prinicipal type");
    }
}
