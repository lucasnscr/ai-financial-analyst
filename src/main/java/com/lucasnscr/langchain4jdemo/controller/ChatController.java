package com.lucasnscr.langchain4jdemo.controller;

import com.lucasnscr.langchain4jdemo.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
