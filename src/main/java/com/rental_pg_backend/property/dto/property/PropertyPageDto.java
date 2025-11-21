package com.rental_pg_backend.property.dto.property;

import java.util.List;

import com.rental_pg_backend.common.dto.PaginationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyPageDto {

    List<PropertyDto> content;

    PaginationDto pagination;

}
