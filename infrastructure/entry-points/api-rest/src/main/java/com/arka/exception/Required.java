package com.arka.exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom annotation to avoid repeating the same validation message
 * manually each time we use @NotNull.
 * Example: "Missing required field: {field}"
 * @code @Required(field = "id")
 */

@NotNull
@Constraint(validatedBy = {})
@Target({ FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
public @interface Required {

    String field();

    String message() default "Missing required field: {field}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
