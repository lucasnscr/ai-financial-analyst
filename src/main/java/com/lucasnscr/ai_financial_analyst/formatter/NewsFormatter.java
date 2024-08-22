package com.lucasnscr.ai_financial_analyst.formatter;

import com.lucasnscr.ai_financial_analyst.llm.model.StockNewsLLM;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class NewsFormatter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

    public String format(String name, StockNewsLLM article) {
        LocalDateTime dateTime = LocalDateTime.parse(article.getTimePublished(), FORMATTER);
        return String.format("""
                        Article Information about: %s
                        Title: %s
                        URL: %s
                        Time: %s
                        Summary: %s
                        Overall Sentiment Score: %.2f
                        Overall Sentiment Label: %s
                        """,
                name, article.getTitle(), article.getUrl(), dateTime,
                article.getSummary(), article.getOverallSentimentScore(),
                article.getOverallSentimentLabel());
    }
}
