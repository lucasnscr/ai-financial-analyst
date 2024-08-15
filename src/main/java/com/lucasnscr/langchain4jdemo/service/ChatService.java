package com.lucasnscr.langchain4jdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @Value("classpath:/prompts/system-chatbot.st")
    private Resource qaSystemPromptResource;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Autowired
    public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    public String question(String message) {
        Message systemMessage = getSystemMessage(message);
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        log.info("Asking AI model to reply to question.");
        ChatResponse chatResponse = chatClient.prompt(prompt).call().chatResponse();
        log.info("AI responded.");
        return chatResponse.getResults().getFirst().getOutput().getContent();
    }

    private Message getSystemMessage(String message) {
        log.info("Retrieving relevant documents");
        List<Document> similarDocuments = vectorStore.similaritySearch(message);
        log.info(String.format("Found %s relevant documents.", similarDocuments.size()));
        String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(this.qaSystemPromptResource);
        return systemPromptTemplate.createMessage(Map.of("documents", documents));
    }
}