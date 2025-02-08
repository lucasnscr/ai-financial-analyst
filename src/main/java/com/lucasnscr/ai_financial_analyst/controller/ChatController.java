package com.lucasnscr.ai_financial_analyst.controller;

import com.lucasnscr.ai_financial_analyst.dto.UserQuestion;
import com.lucasnscr.ai_financial_analyst.exception.InvalidMessageException;
import com.lucasnscr.ai_financial_analyst.service.ChatService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/question")
    public Flux<String> handleUserQuestion(@RequestBody UserQuestion userQuestion) {
        if (StringUtils.isEmpty(userQuestion.getQuestion()) || StringUtils.trim(userQuestion.getQuestion()).isEmpty()) {
            logger.error("Invalid question received: empty or null message");
            return Flux.error(new InvalidMessageException("Question cannot be null or empty"));
        }
        return chatService.processUserQuestion(userQuestion.getQuestion())
                .doOnError(error -> logger.error("Error processing question: {}", error.getMessage()));
    }

    @ExceptionHandler(InvalidMessageException.class)
    public ResponseEntity<String> handleInvalidMessageException(InvalidMessageException e) {
        logger.error("Invalid message error: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage());
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
