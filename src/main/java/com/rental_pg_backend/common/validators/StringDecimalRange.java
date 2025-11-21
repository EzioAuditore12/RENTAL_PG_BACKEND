package com.rental_pg_backend.common.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StringDecimalRange.StringDecimalRangeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringDecimalRange {
    String message() default "Value must be a decimal in the specified range";

    double min();

    double max();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StringDecimalRangeValidator implements ConstraintValidator<StringDecimalRange, String> {
        private double min;
        private double max;

        @Override
        public void initialize(StringDecimalRange constraintAnnotation) {
            min = constraintAnnotation.min();
            max = constraintAnnotation.max();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null)
                return true;
            try {
                double d = Double.parseDouble(value);
                return d >= min && d <= max;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }
}
