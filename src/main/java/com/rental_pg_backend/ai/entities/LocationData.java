package com.rental_pg_backend.ai.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationData {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The city mentioned by the user, e.g., Dehradun")
    private String city;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The state mentioned by the user, e.g., Uttarakhand")
    private String state;

    @JsonProperty(required = false)
    @JsonPropertyDescription("The country mentioned by the user. If not mentioned, default to 'India'")
    private String country = "India";

    @JsonProperty(required = false)
    @JsonPropertyDescription("The specific street, neighborhood, area or landmark mentioned by the user, e.g., Rajpur Road")
    private String street;

    @JsonProperty(required = true)
    @JsonPropertyDescription("""
            Search radius.
            If the user does not mention any radius or distance,
            always return '5km'.
            """)
    private String radius = "5km";

    private Double longitude;

    private Double latitude;
}
