package com.lucasnscr.langchain4jdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LangChain4jDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(LangChain4jDemoApplication.class, args);
    }
}
