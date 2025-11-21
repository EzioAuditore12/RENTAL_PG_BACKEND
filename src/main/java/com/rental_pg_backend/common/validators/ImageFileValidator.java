package com.rental_pg_backend.common.validators;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {

    private boolean required;

    @Override
    public void initialize(ImageFile constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return !required;
        }

        String contentType = file.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("image/")) {
            return true;
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            String lower = filename.toLowerCase();
            return lower.endsWith(".png")
                    || lower.endsWith(".jpg")
                    || lower.endsWith(".jpeg")
                    || lower.endsWith(".gif")
                    || lower.endsWith(".webp")
                    || lower.endsWith(".bmp");
        }

        return false;
    }
}