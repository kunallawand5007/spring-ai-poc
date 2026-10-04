package com.kd.ai.controller;

import com.kd.ai.dto.ChatRequest;
import com.kd.ai.dto.ChatResponse;
import com.kd.ai.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiAssisatedChatController {


    private final ChatService chatService;

    AiAssisatedChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @PostMapping(value = "/api/chat")
    public ResponseEntity<ChatResponse> aiAssisted(@RequestBody ChatRequest chatRequest) {

        ChatResponse chatResponse = chatService.processMessage(chatRequest);
        return new ResponseEntity<>(chatResponse, HttpStatus.OK);

    }

}
