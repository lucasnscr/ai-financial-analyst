package com.lucasnscr.ai_financial_analyst.llm;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LLMContent {

    private static final String TOP_GAINERS = "top_gainers";
    private static final String TOP_LOSERS = "top_losers";
    private static final String MOST_ACTIVELY_TRADED = "most_actively_traded";

    public String prepareNewsLLMContent(String name, JSONArray array, int i) {
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

    public String prepareClassification(String type, JSONArray array, int i) {

        JSONObject jsonObject = array.getJSONObject(i);
        String ticker = jsonObject.getString("ticker");
        Double price = jsonObject.getDouble("price");
        Double changemount = jsonObject.getDouble("change_amount");
        Double changePercentage = jsonObject.getDouble("change_percentage");
        Double volume = jsonObject.getDouble("volume");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(Instant.now().toString(), formatter);
        String classification = "";
        if(TOP_GAINERS.equals(type)){
            classification =  topGainers(ticker, price, changemount, changePercentage, volume, dateTime);
        } else if(TOP_LOSERS.equals(type)){
            classification =  topLosers(ticker, price, changemount, changePercentage, volume, dateTime);
        } else if(MOST_ACTIVELY_TRADED.equals(type)){
            classification =  MostActivelyTraded(ticker, price, changemount, changePercentage, volume, dateTime);
        }
        return classification;
    }

    private static String topGainers(String ticker, Double price, Double changemount, Double changePercentage, Double volume, LocalDateTime dateTime) {
        return String.format("""
                        Top gainers US tickers
                        Ticker: %s
                        Price: %s
                        Change Amount: %s
                        change_percentage: %s
                        Overall Sentiment Score: %.2f
                        Overall Sentiment Label: %s""",
                ticker, price, changemount, changePercentage, volume, dateTime);
    }

    private static String topLosers(String ticker, Double price, Double changemount, Double changePercentage, Double volume, LocalDateTime dateTime) {
        return String.format("""
                        Top losers US tickers
                        Ticker: %s
                        Price: %s
                        Change Amount: %s
                        change_percentage: %s
                        Overall Sentiment Score: %.2f
                        Overall Sentiment Label: %s""",
                ticker, price, changemount, changePercentage, volume, dateTime);
    }

    private static String MostActivelyTraded(String ticker, Double price, Double changemount, Double changePercentage, Double volume, LocalDateTime dateTime) {
        return String.format("""
                        Most actively traded US tickers
                        Ticker: %s
                        Price: %s
                        Change Amount: %s
                        change_percentage: %s
                        Overall Sentiment Score: %.2f
                        Overall Sentiment Label: %s""",
                ticker, price, changemount, changePercentage, volume, dateTime);
    }
}