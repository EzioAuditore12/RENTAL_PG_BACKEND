package com.rental_pg_backend.ai;

import com.rental_pg_backend.ai.dto.DirectPromptDto;
import com.rental_pg_backend.ai.entities.LocationData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ai")
@RequiredArgsConstructor
@Tag(name = "Ai", description = "Chat with models using spring ai")
public class AiController {

    private final AiService aiService;

    @PostMapping
    @Operation(summary = "Prompt Chat", description = "Direct response from model")
    public ResponseEntity<String> askModel(@RequestBody DirectPromptDto directPromptDto) {

        String result = this.aiService.getResponse(directPromptDto);

        return ResponseEntity.ok(result);

    }

    @PostMapping("structured")
    @Operation(summary = "Prompt Chat", description = "Direct response from model")
    public ResponseEntity<LocationData> askStructuredResponse(@RequestBody DirectPromptDto directPromptDto) {

        LocationData result = this.aiService.getStructuredResponse(directPromptDto);

        return ResponseEntity.ok(result);

    }
}
