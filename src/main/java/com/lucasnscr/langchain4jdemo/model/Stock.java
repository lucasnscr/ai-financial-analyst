package com.lucasnscr.langchain4jdemo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "Stocks")
public class Stock {

    @Id
    private String name;
    private String date;
    private List<String> contentforLLM;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getContentforLLM() {
        return contentforLLM;
    }

    public void setContentforLLM(List<String> contentforLLM) {
        this.contentforLLM = contentforLLM;
    }
}
