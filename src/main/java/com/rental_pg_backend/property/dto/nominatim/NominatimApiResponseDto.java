package com.rental_pg_backend.property.dto.nominatim;

import java.util.List;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NominatimApiResponseDto {

    private Long place_id;

    private String licence;

    private String osm_type;

    private Long osm_id;

    private String lat;

    private String lon;

    @JsonProperty("class")
    private String clazz; // 'class' is a reserved word in Java

    private String type;

    private Integer place_rank;

    private Double importance;

    private Address address;

    private String display_name;

    private List<String> boundingbox;
}
