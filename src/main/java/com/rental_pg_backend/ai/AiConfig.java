package com.rental_pg_backend.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean(name = "geminiChatClient")
    ChatClient geminiChatClient(GoogleGenAiChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
