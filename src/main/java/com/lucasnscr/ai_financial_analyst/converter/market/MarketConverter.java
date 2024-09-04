package com.lucasnscr.ai_financial_analyst.converter.market;

import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
import com.lucasnscr.ai_financial_analyst.llm.model.market.StockLLM;
import com.lucasnscr.ai_financial_analyst.model.market.Stock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Component
public class MarketConverter {

    private static final String META_DATA = "Meta Data";
    private static final String TIME_SERIES_DAILY = "Time Series (Daily)";
    private final LLMContent llmContent;

    public MarketConverter(LLMContent llmContent){
        this.llmContent = llmContent;
    }

    public Stock convertJsonToStockData(String name, JSONObject jsonResponse) {
        if (ObjectUtils.isEmpty(jsonResponse)) {
            return new Stock();
        }
        JSONObject metaData = jsonResponse.getJSONObject(META_DATA);
        String date = metaData.getString("3. Last Refreshed");
        return new Stock(name, date, buildStockDays(name, jsonResponse));
    }

    private List<String> buildStockDays(String name, JSONObject jsonResponse) {
        List<String> llmContentList = null;
        JSONObject timeSeriesJson = jsonResponse.getJSONObject("Time Series (Daily)");
        if (!ObjectUtils.isEmpty(timeSeriesJson)){
            llmContentList = new ArrayList<>();
            for (String date : timeSeriesJson.keySet()) {
                JSONObject dailyJson = timeSeriesJson.getJSONObject(date);
                String content = llmContent.prepareStockMarketData(name, date, dailyJson);
                llmContentList.add(content);
            }
        }
        return llmContentList;
    }
}