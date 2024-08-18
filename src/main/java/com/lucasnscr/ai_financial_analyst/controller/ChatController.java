package com.lucasnscr.ai_financial_analyst.controller;

import com.lucasnscr.ai_financial_analyst.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;
    }

    @PostMapping("/question")
    public ResponseEntity<String> getQuestion(@RequestBody String message){
        return ResponseEntity.ok().body(chatService.question(message));
    }
}
