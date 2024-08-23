package com.lucasnscr.ai_financial_analyst.llm;

import com.lucasnscr.ai_financial_analyst.formatter.NewsFormatter;
import com.lucasnscr.ai_financial_analyst.formatter.StockClassificationFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.StockClassficationLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.StockCryptoNewsLLM;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class LLMContent {

    private final NewsFormatter newsFormatter;
    private final StockClassificationFormatter classificationFormatter;

    public LLMContent(NewsFormatter newsFormatter, StockClassificationFormatter classificationFormatter) {
        this.newsFormatter = newsFormatter;
        this.classificationFormatter = classificationFormatter;
    }

    public String prepareNewsLLMContent(String name, JSONArray array, int i) {
        JSONObject jsonObject = array.getJSONObject(i);
        StockCryptoNewsLLM article = new StockCryptoNewsLLM(
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
        StockClassficationLLM stockClassficationLLM = new StockClassficationLLM(
                jsonObject.getString("ticker"),
                jsonObject.getDouble("price"),
                jsonObject.getDouble("change_amount"),
                jsonObject.getString("change_percentage"),
                jsonObject.getDouble("volume")
        );
        return classificationFormatter.format(type, stockClassficationLLM);
    }
}