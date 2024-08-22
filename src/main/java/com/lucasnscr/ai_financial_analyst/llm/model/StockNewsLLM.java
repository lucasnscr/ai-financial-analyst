package com.lucasnscr.ai_financial_analyst.llm.model;

import lombok.Data;

@Data
public class StockNewsLLM {

    private final String title;
    private final String url;
    private final String timePublished;
    private final String summary;
    private final double overallSentimentScore;
    private final String overallSentimentLabel;


    public StockNewsLLM(String title, String url, String timePublished, String summary, double overallSentimentScore, String overallSentimentLabel) {
        this.title = title;
        this.url = url;
        this.timePublished = timePublished;
        this.summary = summary;
        this.overallSentimentScore = overallSentimentScore;
        this.overallSentimentLabel = overallSentimentLabel;
    }
}
