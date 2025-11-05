package com.pratham.bootbase.controller;

import com.pratham.bootbase.dto.Request.AddEmployeeDTO;
import com.pratham.bootbase.dto.Request.PatchEmployeeDTO;
import com.pratham.bootbase.dto.ApiResponse;
import com.pratham.bootbase.dto.Response.DeleteDTO;
import com.pratham.bootbase.dto.Response.GetEmployeeDTO;
import com.pratham.bootbase.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Validated
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<GetEmployeeDTO>> getById(@Min(1) @PathVariable Long id){
        GetEmployeeDTO employeeDTO = employeeService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("employee retrieved successfully",employeeDTO));
    }

    //@valid works without @validated
    //@valid is used to ensure validation on a request body dto object
    //to ensure bean validation on path variables or query parameters we have to add @validated on the controller
    @PostMapping("/")
    ResponseEntity<ApiResponse<GetEmployeeDTO>> add(@Valid @RequestBody AddEmployeeDTO addEmployeeDTO){
        GetEmployeeDTO employeeDTO = employeeService.add(addEmployeeDTO);
        return ResponseEntity.ok(new ApiResponse<>("employee added successfully",employeeDTO));
    }

    @GetMapping("/")
    ResponseEntity<ApiResponse<PagedModel<GetEmployeeDTO>>> getAll(@Min(value = 0,message = "page num should be minimum 0") @RequestParam(defaultValue = "0") Integer num, @Min(1) @RequestParam(defaultValue = "10") Integer size){
        PagedModel<GetEmployeeDTO> employeeDTOPagedModel = employeeService.getAll(num,size);
        ApiResponse<PagedModel<GetEmployeeDTO>> apiResponse = new ApiResponse<>("employees retrieved successfully",employeeDTOPagedModel);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<GetEmployeeDTO>> put(@Min(1) @PathVariable Long id, @Valid @RequestBody AddEmployeeDTO addEmployeeDTO){
        GetEmployeeDTO employeeDTO = employeeService.put(id,addEmployeeDTO);
        return ResponseEntity.ok(new ApiResponse<>("employee updated successfully",employeeDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<GetEmployeeDTO>> patch(@PathVariable Long id, @Valid @RequestBody PatchEmployeeDTO patchEmployeeDTO){
        GetEmployeeDTO employeeDTO = employeeService.patch(id,patchEmployeeDTO);
        return ResponseEntity.ok(new ApiResponse<>("employee updated successfully",employeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeleteDTO>> delete(@PathVariable Long id){
        DeleteDTO dto = employeeService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Employee deleted successfully",dto));
    }
}
