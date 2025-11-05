package com.pratham.bootbase.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private LocalDate joiningDate;
    private Boolean isActive;
}
