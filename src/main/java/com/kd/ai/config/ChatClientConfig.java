package com.kd.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {



    @Bean
    ChatClient chatClient(ChatModel chatModel,FileTransferTool fileTransferTool){
        return ChatClient.builder(chatModel)
                .defaultSystem("You are a helpful customer support agent. " +
                        "You can help customers with file transfer status inquiries and retry file for failed transmission. ")
                .defaultTools(fileTransferTool)
                .defaultAdvisors()
                .build();
    }

}
