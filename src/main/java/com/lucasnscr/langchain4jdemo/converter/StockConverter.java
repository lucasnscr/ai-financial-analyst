package com.lucasnscr.langchain4jdemo.converter;

import com.lucasnscr.langchain4jdemo.model.Stock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class StockConverter {

    public Stock convertJsonToLastStock(JSONObject jsonResponse) {
        String tickerName = jsonResponse.getString("ticker");
        JSONArray results = jsonResponse.getJSONArray("results");

        JSONObject lastResult = results.getJSONObject(results.length() - 1);
        Stock stock = new Stock();

        stock.setName(tickerName);
        stock.setVolume(lastResult.getLong("v"));
        stock.setVolumeWeightedAveragePrice(lastResult.getDouble("vw"));
        stock.setPriceOpenMarket(lastResult.getDouble("o"));
        stock.setPriceCloseMarket(lastResult.getDouble("c"));
        stock.setPriceHighest(lastResult.getDouble("h"));
        stock.setPriceLess(lastResult.getDouble("l"));
        stock.setTransactionsDay(lastResult.getLong("n"));

        long timestamp = lastResult.getLong("t");
        LocalDate date = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        stock.setDate(date);

        return stock;
    }

}
