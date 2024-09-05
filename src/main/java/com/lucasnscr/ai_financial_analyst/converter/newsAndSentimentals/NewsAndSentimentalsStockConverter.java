package com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals;

import com.lucasnscr.ai_financial_analyst.formatter.newsAndSentimentals.NewsFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.newsAndSentimentals.NewsAndSentimentalsLLM;
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
public class NewsAndSentimentalsStockConverter {

    private final NewsFormatter newsFormatter;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public NewsAndSentimentalsStockConverter(NewsFormatter newsFormatter) {this.newsFormatter = newsFormatter;}

    public StockNewsAndSentimentals convertJsonToStock(String name, JSONObject jsonResponse) {
        return new StockNewsAndSentimentals(
                name,
                getCurrentFormattedDate(),
                buildContentListLLM(name, jsonResponse)
        );
    }

    private String getCurrentFormattedDate() {
        return LocalDate.now().format(FORMATTER);
    }

    private List<String> buildContentListLLM(String name, JSONObject sentiments) {
        JSONArray feed = sentiments.optJSONArray("feed");
        if (ObjectUtils.isEmpty(feed)) {
            return List.of();
        }
        return IntStream.range(0, feed.length())
                .mapToObj(i -> prepareNewsLLMContent(name, feed, i))
                .collect(Collectors.toList());
    }

    public String prepareNewsLLMContent(String name, JSONArray array, int i) {
        JSONObject jsonObject = array.getJSONObject(i);
        NewsAndSentimentalsLLM article = new NewsAndSentimentalsLLM(
                jsonObject.getString("title"),
                jsonObject.getString("url"),
                jsonObject.getString("time_published"),
                jsonObject.getString("summary"),
                jsonObject.getDouble("overall_sentiment_score"),
                jsonObject.getString("overall_sentiment_label")
        );
        return newsFormatter.format(name, article);
    }
}