package com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals;

import com.lucasnscr.ai_financial_analyst.formatter.newsAndSentimentals.NewsFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.newsAndSentimentals.NewsAndSentimentalsLLM;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
public class NewsAndSentimentalsConverter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final NewsFormatter newsFormatter;

    public NewsAndSentimentalsConverter(NewsFormatter newsFormatter) {
        this.newsFormatter = newsFormatter;
    }

    public StockNewsAndSentimentals convertJsonToStock(String name, JSONObject jsonResponse) {
        return new StockNewsAndSentimentals(
                name,
                getCurrentFormattedDate(),
                extractContentList(name, jsonResponse)
        );
    }

    public CryptoNewsAndSentimentals convertJsonToCrypto(String name, JSONObject jsonResponse) {
        return new CryptoNewsAndSentimentals(
                name,
                getCurrentFormattedDate(),
                extractContentList(name, jsonResponse)
        );
    }

    private String getCurrentFormattedDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    private List<String> extractContentList(String name, JSONObject sentiments) {
        JSONArray feed = sentiments.optJSONArray("feed");
        if (ObjectUtils.isEmpty(feed)) {
            return List.of();
        }
        return IntStream.range(0, feed.length())
                .mapToObj(i -> prepareNewsContent(name, feed.getJSONObject(i)))
                .collect(Collectors.toList());
    }

    private String prepareNewsContent(String name, JSONObject jsonObject) {
        NewsAndSentimentalsLLM article = extractNewsAndSentimentals(jsonObject);
        return newsFormatter.format(name, article);
    }

    private NewsAndSentimentalsLLM extractNewsAndSentimentals(JSONObject jsonObject) {
        return new NewsAndSentimentalsLLM(
                jsonObject.optString("title", "No Title"),
                jsonObject.optString("url", "No URL"),
                jsonObject.optString("time_published", "No Time Published"),
                jsonObject.optString("summary", "No Summary"),
                jsonObject.optDouble("overall_sentiment_score", 0.0),
                jsonObject.optString("overall_sentiment_label", "Neutral")
        );
    }
}
