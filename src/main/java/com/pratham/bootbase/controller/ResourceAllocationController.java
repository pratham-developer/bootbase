package com.pratham.bootbase.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceAllocationController {

    @PreAuthorize("hasAnyAuthority('FREE','BASIC','PREMIUM')")
    @GetMapping("/free")
    public ResponseEntity<Void> allocateFree(){
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('BASIC','PREMIUM')")
    @GetMapping("/basic")
    public ResponseEntity<Void> allocateBasic(){
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('PREMIUM')")
    @GetMapping("/premium")
    public ResponseEntity<Void> allocatePremium(){
        return ResponseEntity.noContent().build();
    }
}
