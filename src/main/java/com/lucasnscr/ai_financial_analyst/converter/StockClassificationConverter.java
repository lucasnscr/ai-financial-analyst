package com.lucasnscr.ai_financial_analyst.converter;

import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
import com.lucasnscr.ai_financial_analyst.model.StockClassification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockClassificationConverter {

    private static final String TOP_GAINERS = "top_gainers";
    private static final String TOP_LOSERS = "top_losers";
    private static final String MOST_ACTIVELY_TRADED = "most_actively_traded";

    private final LLMContent llmContent;

    public StockClassificationConverter(LLMContent llmContent) {
        this.llmContent = llmContent;
    }

    public StockClassification convertJsonToStockClassification(JSONObject jsonResponse) {
        StockClassification stockClassification = new StockClassification();
        if (!ObjectUtils.isEmpty(jsonResponse)){
            stockClassification.setDate(jsonResponse.getString("last_updated"));
            stockClassification.setClassifications(buildStockClassification(jsonResponse));
        }
        return stockClassification;
    }

    private List<String> buildStockClassification(JSONObject classification) {
        List<String> classifications = new ArrayList<>();
        JSONArray topGainers = classification.getJSONArray("top_gainers");
        JSONArray topLosers = classification.getJSONArray("top_losers");
        JSONArray mostActivelyTraded = classification.getJSONArray("most_actively_traded");
        buildStringClassification(classifications, TOP_GAINERS, topGainers);
        buildStringClassification(classifications, TOP_LOSERS, topLosers);
        buildStringClassification(classifications, MOST_ACTIVELY_TRADED, mostActivelyTraded);
        return classifications;
    }

    private void buildStringClassification(List<String> classifications, String type, JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            if (!ObjectUtils.isEmpty(jsonArray)){
                classifications.add(llmContent.prepareClassification(type, jsonArray, i));
            }
        }
    }

}
