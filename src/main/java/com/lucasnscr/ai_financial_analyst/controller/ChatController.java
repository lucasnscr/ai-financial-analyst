package com.lucasnscr.ai_financial_analyst.controller;

import com.lucasnscr.ai_financial_analyst.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @PostMapping("/question")
    public Flux<String> getQuestion(@RequestBody String message){
        return chatService.question(message);
    }
}
