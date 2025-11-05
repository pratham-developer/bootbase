package com.pratham.bootbase.dto.Response;

import com.pratham.bootbase.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllEmployeeDTO {
    private List<Employee> employees;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;

}
