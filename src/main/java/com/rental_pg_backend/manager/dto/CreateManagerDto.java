package com.rental_pg_backend.manager.dto;

import org.springframework.web.multipart.MultipartFile;

import com.rental_pg_backend.common.validators.ImageFile;
import com.rental_pg_backend.common.validators.StrongPassword;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateManagerDto {

    @NotBlank
    @Size(max = 50)
    @Schema(example = "Saitama")
    private String name;

    @NotBlank
    @Email
    @Size(max = 240)
    @Schema(example = "example@example.com")
    private String email;

    @NotBlank
    @StrongPassword
    @Size(max = 16)
    @Schema(example = "Example@123")
    private String password;

    @ImageFile(message = "Avatar must be an image (png, jpg, gif, webp, bmp).", required = false)
    private MultipartFile avatar;

    public void setEmail(String email) {
        this.email = (email != null) ? email.toLowerCase() : null;
    }
}
