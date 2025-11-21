package com.rental_pg_backend.manager.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerIdPasswordDto {

    private UUID id;

    private String password;

}
