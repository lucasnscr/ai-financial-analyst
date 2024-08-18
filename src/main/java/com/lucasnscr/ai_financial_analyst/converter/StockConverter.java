package com.lucasnscr.ai_financial_analyst.converter;

import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
import com.lucasnscr.ai_financial_analyst.model.Stock;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class StockConverter {

    private final LLMContent llmContent;

    @Autowired
    public StockConverter(LLMContent llmContent) {
        this.llmContent = llmContent;
    }

    public Stock convertJsonToStock(String name, JSONObject jsonResponse) {
        Stock stock = new Stock();
        stock.setName(name);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        stock.setDate(currentDate.format(formatter));
        stock.setContentforLLM(buildContentListLLM(name, jsonResponse));
        return stock;
    }

    private List<String> buildContentListLLM(String name, JSONObject sentiments) {
        List<String> contentList = new ArrayList<>();
        String llmContent = null;
        for (int i = 0; i < sentiments.length(); i++) {
            JSONArray feed = sentiments.getJSONArray("feed");
            if (ObjectUtils.isEmpty(feed)){
                llmContent = this.llmContent.prepareLLMContent(name, feed, i);
            }
            contentList.add(llmContent);
        }
        return contentList;
    }
}
