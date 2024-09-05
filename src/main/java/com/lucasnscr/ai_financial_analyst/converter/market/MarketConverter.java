package com.lucasnscr.ai_financial_analyst.converter.market;

import com.lucasnscr.ai_financial_analyst.formatter.market.StockFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.market.StockLLM;
import com.lucasnscr.ai_financial_analyst.model.market.Stock;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class MarketConverter {

    private static final String META_DATA = "Meta Data";
    private final StockFormatter stockFormatter;

    public MarketConverter(StockFormatter stockFormatter){
        this.stockFormatter = stockFormatter;
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
                String content = prepareStockMarketData(name, date, dailyJson);
                llmContentList.add(content);
            }
        }
        return llmContentList;
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