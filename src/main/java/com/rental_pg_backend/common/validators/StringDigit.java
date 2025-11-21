package com.rental_pg_backend.common.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringDigit.StringDigitValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringDigit {
    String message() default "Value must contain only digits";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StringDigitValidator implements ConstraintValidator<StringDigit, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return value == null || value.matches("\\d+");
        }
    }
}
