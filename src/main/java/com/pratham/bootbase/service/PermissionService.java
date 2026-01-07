package com.pratham.bootbase.service;

import com.pratham.bootbase.repository.EmployeeRepository;
import com.pratham.bootbase.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final EmployeeRepository employeeRepository;

    public boolean isOwnerOfEmployee(Long employeeId){
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return employeeRepository.existsByIdAndOwnerId(employeeId,currentUserId);
    }
}
