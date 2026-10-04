package com.kd.ai.service;

import com.kd.ai.dto.ChatRequest;
import com.kd.ai.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chBuilder) {
        this.chatClient = chBuilder.build();
    }

    public ChatResponse processMessage(ChatRequest chatRequest) {
        try {

            String chatResponse = chatClient.prompt()
                    .user(chatRequest.message())
                    .call()
                    .content();

            return new ChatResponse(chatResponse);


        } catch (Exception e) {
            e.printStackTrace();
            log.info("exception occuured while promting...", e);
            throw new RuntimeException("Sorry, error occurred while processing your request ..");
        }

    }
}
