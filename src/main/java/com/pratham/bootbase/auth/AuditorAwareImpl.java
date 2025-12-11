package com.pratham.bootbase.auth;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //TODO: fetch current user name from spring security
        return Optional.of("Ekansh");
    }
}
