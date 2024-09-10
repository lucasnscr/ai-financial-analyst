package com.lucasnscr.ai_financial_analyst.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class VectorDbConfig {

    @Value("${spring.ai.openai.embedding.api-key}")
    private String openAiApiKey;

    @Bean(name = "vectorStoreDB")
    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, OpenAiEmbeddingModel embeddingModel) {
        return new PgVectorStore(jdbcTemplate, embeddingModel);
    }

    @Bean
    public OpenAiEmbeddingModel embeddingModel(){
        var openAiApi = new OpenAiApi(openAiApiKey);
        return new OpenAiEmbeddingModel(
                openAiApi,
                MetadataMode.EMBED,
                OpenAiEmbeddingOptions.builder()
                        .withModel("text-embedding-ada-002")
                        .withUser("user-6")
                        .build(),
                RetryUtils.DEFAULT_RETRY_TEMPLATE);

    }

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }


}
