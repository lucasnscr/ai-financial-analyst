package com.lucasnscr.ai_financial_analyst.llm;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

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
        String changePercentage = jsonObject.getString("change_percentage");
        Double volume = jsonObject.getDouble("volume");
        String classification = "";
        if(TOP_GAINERS.equals(type)){
            classification =  topGainers(ticker, price, changemount, changePercentage, volume);
        } else if(TOP_LOSERS.equals(type)){
            classification =  topLosers(ticker, price, changemount, changePercentage, volume);
        } else if(MOST_ACTIVELY_TRADED.equals(type)){
            classification =  MostActivelyTraded(ticker, price, changemount, changePercentage, volume);
        }
        return classification;
    }

    private static String topGainers(String ticker, Double price, Double changemount, String changePercentage, Double volume) {
        return String.format("""
                        Top Gainers US tickers
                        Ticker: %s
                        Price: %s
                        Change Amount: %s
                        Change Percentage: %s
                        Volume: %.2f
                        """,
                ticker, price, changemount, changePercentage, volume);
    }

    private static String topLosers(String ticker, Double price, Double changemount, String changePercentage, Double volume) {
        return String.format("""
                        Top Losers US tickers
                        Ticker: %s
                        Price: %s
                        Change Amount: %s
                        Change Percentage: %s
                        Volume: %.2f
                        """,
                ticker, price, changemount, changePercentage, volume);
    }

    private static String MostActivelyTraded(String ticker, Double price, Double changemount, String changePercentage, Double volume) {
        return String.format("""
                        Most Actively Traded US tickers
                        Ticker: %s
                        Price: %s
                        Change Amount: %s
                        Change Percentage: %s
                        Volume: %.2f
                        """,
                ticker, price, changemount, changePercentage, volume);
    }
}