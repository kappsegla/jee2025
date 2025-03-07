package org.fungover.jee2025.rules;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidBookValidator.class})
public @interface ValidBook {
    String message() default "Not a valid book";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
