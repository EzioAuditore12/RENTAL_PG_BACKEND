package com.rental_pg_backend.common.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringDecimal.StringDecimalValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringDecimal {
    String message() default "Value must be a valid decimal number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StringDecimalValidator implements ConstraintValidator<StringDecimal, String> {
        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return true;
            try {
                Double.parseDouble(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
