package com.kd.ai.service;

import com.kd.ai.dto.ChatRequest;
import com.kd.ai.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder chBuilder, VectorStore vectorStore) {
      //  this.chatClient = chBuilder.build(); // this for simple chat client
        // for handling RAG
        this.chatClient = chBuilder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().build())
                        .build())
                .build();

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
