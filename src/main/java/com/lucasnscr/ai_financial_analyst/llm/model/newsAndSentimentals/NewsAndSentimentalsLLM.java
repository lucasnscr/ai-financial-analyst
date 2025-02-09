package com.lucasnscr.ai_financial_analyst.llm.model.newsAndSentimentals;

import lombok.Data;

@Data
public class NewsAndSentimentalsLLM {
    private final String title;
    private final String url;
    private final String timePublished;
    private final String summary;
    private final double overallSentimentScore;
    private final String overallSentimentLabel;
}
