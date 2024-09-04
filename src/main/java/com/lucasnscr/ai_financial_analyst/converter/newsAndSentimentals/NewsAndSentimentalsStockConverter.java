package com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals;

import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
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

    private final LLMContent llmContent;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public NewsAndSentimentalsStockConverter(LLMContent llmContent) {
        this.llmContent = llmContent;
    }

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
                .mapToObj(i -> llmContent.prepareNewsLLMContent(name, feed, i))
                .collect(Collectors.toList());
    }
}