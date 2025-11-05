package com.pratham.bootbase.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratham.bootbase.dto.Request.AddEmployeeDTO;
import com.pratham.bootbase.dto.Request.PatchEmployeeDTO;
import com.pratham.bootbase.dto.Response.DeleteDTO;
import com.pratham.bootbase.dto.Response.GetEmployeeDTO;
import com.pratham.bootbase.entity.Employee;
import com.pratham.bootbase.exception.BadRequestException;
import com.pratham.bootbase.exception.ResourceNotFoundException;
import com.pratham.bootbase.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;
    private final ObjectMapper objectMapper;

    public GetEmployeeDTO getById(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee Not Found"));
        return mapper.map(employee, GetEmployeeDTO.class);
    }

    @Transactional
    public GetEmployeeDTO add(AddEmployeeDTO addEmployeeDTO){
        if(employeeRepository.existsByEmail(addEmployeeDTO.getEmail())){
            throw new BadRequestException("email already exists");
        }

        Employee toSave = mapper.map(addEmployeeDTO, Employee.class);
        Employee saved = employeeRepository.save(toSave);
        return mapper.map(saved, GetEmployeeDTO.class);
    }

    public PagedModel<GetEmployeeDTO> getAll(Integer num, Integer size){
        Pageable pageable = PageRequest.of(num,size);
        Page<Employee> employeesPage = employeeRepository.findAll(pageable);
        Page<GetEmployeeDTO> dtoPage = employeesPage.map(employee -> mapper.map(employee, GetEmployeeDTO.class));
        return new PagedModel<>(dtoPage);

    }

    @Transactional
    public GetEmployeeDTO put(Long id, AddEmployeeDTO addEmployeeDTO){
        Employee toUpdate = employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee ID Not Found"));
        //save function saves the item if new
        //otherwise it updates the item
        mapper.map(addEmployeeDTO,toUpdate);
        Employee updated = employeeRepository.save(toUpdate);
        return mapper.map(updated,GetEmployeeDTO.class);
    }

    @Transactional
    public GetEmployeeDTO patch(Long id, PatchEmployeeDTO patchEmployeeDTO){
        Employee toUpdate = employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee ID Not Found"));
        //update with jackson mapper which just updates with non-null values
        try {
            objectMapper.updateValue(toUpdate,patchEmployeeDTO);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
        Employee updated = employeeRepository.save(toUpdate);
        return mapper.map(updated, GetEmployeeDTO.class);
    }

    @Transactional
    public DeleteDTO delete(Long id){
        Employee toDelete = employeeRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("Employee ID Not Found")
                );

        employeeRepository.delete(toDelete);
        return new DeleteDTO(true);

    }
}
