package com.lucasnscr.ai_financial_analyst.llm;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LLMContent {

    public String prepareLLMContent(String name, JSONArray array, int i) {

        JSONObject jsonObject = array.getJSONObject(i);

        String title = jsonObject.getString("title");
        String url = jsonObject.getString("url");
        String timePublishedStr = jsonObject.getString("time_published");
        String summary = jsonObject.getString("summary");
        double overallSentimentScore = jsonObject.getDouble("overall_sentiment_score");
        String overallSentimentLabel = jsonObject.getString("overall_sentiment_label");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(timePublishedStr, formatter);

        return String.format("""
                        Article Information about: %s
                        Title: %s
                        url: %s
                        Time: %s
                        Summary: %s
                        Overall Sentiment Score: %.2f
                        Overall Sentiment Label: %s""",
                name, title, url, dateTime, summary, overallSentimentScore, overallSentimentLabel);
    }
}