package com.lucasnscr.langchain4jdemo.config;

import com.lucasnscr.langchain4jdemo.model.AiAssistantModelCommunication;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ModelConfig {

    private static final String OLLAMA_HOST = "http://localhost:11434";
    private static final String OLLAMA_MODEL = "llama3.1:latest";

    @Bean
    public static StreamingChatLanguageModel connectModel() {
        return OllamaStreamingChatModel.builder()
                .temperature(0.2)
                .baseUrl(OLLAMA_HOST)
                .modelName(OLLAMA_MODEL)
                .timeout(Duration.ofHours(1))
                .build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    public AiAssistantModelCommunication modelCommunication(){
        return AiServices.builder(AiAssistantModelCommunication.class)
                .streamingChatLanguageModel(connectModel())
                .chatMemory(chatMemory())
                .build();
    }
}
