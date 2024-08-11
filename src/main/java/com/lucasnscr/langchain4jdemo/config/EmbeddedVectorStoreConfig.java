package com.lucasnscr.langchain4jdemo.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class EmbeddedVectorStoreConfig {

    @Value("${database.host}")
    private String host;

    @Value("${database.port}")
    private int port;

    @Value("${database.name}")
    private String databaseName;

    @Value("${database.user}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Value("${database.table}")
    private String table;

    @Value("${database.dimension}")
    private int dimension;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return PgVectorEmbeddingStore.builder()
                .host(host)
                .port(port)
                .database(databaseName)
                .user(username)
                .password(password)
                .table(table)
                .dimension(dimension)
                .build();
    }

}
