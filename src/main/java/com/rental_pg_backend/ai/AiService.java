package com.rental_pg_backend.ai;

import com.rental_pg_backend.ai.dto.DirectPromptDto;
import com.rental_pg_backend.ai.entities.LocationData;
import com.rental_pg_backend.property.dto.nominatim.NominatimApiResponseDto;
import com.rental_pg_backend.property.utils.NominatimUtils;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final ChatClient chatClient;

    public AiService(@Qualifier("geminiChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String getResponse(DirectPromptDto directPromptDto) {

        String queryStr = "Answer any question in good way {query}";

        return chatClient.
                prompt().
                user(u -> u.text(queryStr).param("query", directPromptDto.getPrompt())).
                call().
                content();
    }

    public LocationData getStructuredResponse(DirectPromptDto directPromptDto) {

        String prompt = """
                Extract the location information from the user query.
                
                Rules:
                - If radius is not mentioned, return '5km'
                - Always return valid JSON
                - Extract city and state properly
                - Extract specific street, neighborhood, area or landmark mentioned by the user into the 'street' field
                - If no country is mentioned, assume 'India'
                
                User Query:
                """ + directPromptDto.getPrompt();

        Prompt promptMessage = new Prompt(prompt);

        LocationData locationData = chatClient.prompt(promptMessage).call().entity(LocationData.class);

        if (locationData != null) {
            try {
                com.rental_pg_backend.property.dto.nominatim.NominatimSearchLocationDto searchLocationDto = 
                        new com.rental_pg_backend.property.dto.nominatim.NominatimSearchLocationDto();
                searchLocationDto.setStreet(locationData.getStreet());
                searchLocationDto.setCity(locationData.getCity());
                searchLocationDto.setState(locationData.getState());
                searchLocationDto.setCountry(locationData.getCountry() != null ? locationData.getCountry() : "India");
                
                NominatimApiResponseDto geoDetails = 
                        NominatimUtils.getGeoLocationDetails(searchLocationDto);
                        
                if (geoDetails != null) {
                    if (geoDetails.getLat() != null) {
                        locationData.setLatitude(Double.parseDouble(geoDetails.getLat()));
                    }
                    if (geoDetails.getLon() != null) {
                        locationData.setLongitude(Double.parseDouble(geoDetails.getLon()));
                    }
                }
            } catch (Exception e) {
                System.err.println("Failed to fetch coordinates for location: " + e.getMessage());
            }
        }

        return locationData;
    }

}
