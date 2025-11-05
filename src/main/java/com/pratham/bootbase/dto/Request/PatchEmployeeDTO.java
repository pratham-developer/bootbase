package com.pratham.bootbase.dto.Request;

import com.pratham.bootbase.annotation.NullOrNotBlank;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatchEmployeeDTO {

    //custom annotation
    @NullOrNotBlank(message = "name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NullOrNotBlank(message = "email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 65, message = "Age must not exceed 65")
    private Integer age;

    private LocalDate joiningDate;

    private Boolean isActive;
}
