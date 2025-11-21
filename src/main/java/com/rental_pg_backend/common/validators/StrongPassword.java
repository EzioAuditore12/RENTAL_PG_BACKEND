package com.rental_pg_backend.common.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPassword.StrongPasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Password must be at least 8 characters long and contain an uppercase letter, a lowercase letter, a digit, and a special character";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {
        @Override
        public boolean isValid(String password, ConstraintValidatorContext context) {
            if (password == null)
                return false;
            return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
        }
    }
}