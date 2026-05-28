package com.rental_pg_backend.ai;

import com.rental_pg_backend.ai.dto.DirectPromptDto;
import com.rental_pg_backend.ai.entities.LocationData;
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
                
                User Query:
                """ + directPromptDto.getPrompt();

        Prompt promptMessage = new Prompt(prompt);

        return chatClient.prompt(promptMessage).call().entity(LocationData.class);
    }

}
