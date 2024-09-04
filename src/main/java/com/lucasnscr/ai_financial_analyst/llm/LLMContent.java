package com.lucasnscr.ai_financial_analyst.llm;

import com.lucasnscr.ai_financial_analyst.formatter.market.StockFormatter;
import com.lucasnscr.ai_financial_analyst.formatter.newsAndSentimentals.NewsFormatter;
import com.lucasnscr.ai_financial_analyst.formatter.classification.ClassificationFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.classification.ClassficationLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.market.StockLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.newsAndSentimentals.NewsAndSentimentalsLLM;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class LLMContent {

    private final NewsFormatter newsFormatter;
    private final ClassificationFormatter classificationFormatter;
    private final StockFormatter stockFormatter;

    public LLMContent(NewsFormatter newsFormatter,
                      ClassificationFormatter classificationFormatter,
                      StockFormatter stockFormatter) {
        this.newsFormatter = newsFormatter;
        this.classificationFormatter = classificationFormatter;
        this.stockFormatter = stockFormatter;
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

    public String prepareClassification(String type, JSONArray array, int i) {
        JSONObject jsonObject = array.getJSONObject(i);
        ClassficationLLM classficationLLM = new ClassficationLLM(
                jsonObject.getString("ticker"),
                jsonObject.getDouble("price"),
                jsonObject.getDouble("change_amount"),
                jsonObject.getString("change_percentage"),
                jsonObject.getDouble("volume")
        );
        return classificationFormatter.format(type, classficationLLM);
    }

    public String prepareStockMarketData(String name, String date, JSONObject jsonObject) {
        StockLLM stockLLM = getStockLLM(name, date, jsonObject);
        return stockFormatter.format(name, stockLLM);
    }

    private StockLLM getStockLLM(String name, String date, JSONObject jsonObject) {
        StockLLM stockLLM = new StockLLM();
        stockLLM.setName(name);
        stockLLM.setDate(date);
        stockLLM.setPriceOpen(jsonObject.getString("1. open"));
        stockLLM.setPriceHigh(jsonObject.getString("2. high"));
        stockLLM.setPriceLow(jsonObject.getString("3. low"));
        stockLLM.setPriceClose(jsonObject.getString("4. close"));
        stockLLM.setPriceAdjustedClose(jsonObject.getString("5. adjusted close"));
        stockLLM.setVolume(jsonObject.getString("6. volume"));
        stockLLM.setDividendAmount(jsonObject.getString("7. dividend amount"));
        stockLLM.setCoefficient(jsonObject.getString("8. split coefficient"));
        return stockLLM;
    }
}