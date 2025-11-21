package com.rental_pg_backend.common.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Constraint(validatedBy = ImageFileValidator.class)
@Documented
public @interface ImageFile {
    String message() default "File must be an image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * If true the file is required (non-null and non-empty). Default false allows
     * null/empty.
     */
    boolean required() default false;
}