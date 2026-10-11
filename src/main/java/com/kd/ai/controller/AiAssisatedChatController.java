package com.kd.ai.controller;

import com.kd.ai.dto.ChatRequest;
import com.kd.ai.dto.ChatResponse;
import com.kd.ai.service.ChatService;
import com.kd.ai.service.RagIngestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

@RestController
@Slf4j
public class AiAssisatedChatController {


    private final ChatService chatService;
    private final VectorStore vectorStore;
   private final RagIngestionService ragIngestionService;

    AiAssisatedChatController(ChatService chatService, VectorStore vectorStore, RagIngestionService ragIngestionService) {
        this.chatService = chatService;
        this.vectorStore = vectorStore;
        this.ragIngestionService = ragIngestionService;
    }


    @PostMapping(value = "/api/chat")
    public ResponseEntity<ChatResponse> aiAssisted(@RequestBody ChatRequest chatRequest) {
        ChatResponse chatResponse = chatService.processMessage(chatRequest);
        return new ResponseEntity<>(chatResponse, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVector(@RequestBody String input){
        log.info("Corrective store configure:"+vectorStore);
       // add document
        vectorStore.add(List.of(new Document(input)));
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }

    @PostMapping(value = "/document/upload")
    public ResponseEntity<List<Document>> uploadDocument(@RequestPart("file")MultipartFile file) {
        List<Document> documents = ragIngestionService.uploadVector(file);
        return new ResponseEntity<>(documents,HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Document>> searchDocument(@RequestParam String search){
        SearchRequest build = SearchRequest.builder().query(search).topK(1).similarityThreshold(0.85).build();
        return new ResponseEntity<>(vectorStore.similaritySearch(build),HttpStatus.OK);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteEmbedding(@PathVariable String id){
        vectorStore.delete(List.of(id));
        return new ResponseEntity<>("Deleted..",HttpStatus.OK);
    }

}
