package com.pratham.bootbase.annotation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Constraint(
        validatedBy = {NullOrNotBlankValidator.class}
)
@Target({ ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrNotBlank {
    String message() default "string cannot be blank.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
