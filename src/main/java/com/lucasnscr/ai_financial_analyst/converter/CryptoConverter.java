package com.lucasnscr.ai_financial_analyst.converter;

import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
import com.lucasnscr.ai_financial_analyst.model.Crypto;
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
public class CryptoConverter {

    private final LLMContent llmContent;

    @Autowired
    public CryptoConverter(LLMContent llmContent) {
        this.llmContent = llmContent;
    }

    public Crypto convertJsonToCrypto(String name, JSONObject jsonResponse) {
        Crypto crypto = new Crypto();
        crypto.setName(name);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        crypto.setDate(currentDate.format(formatter));
        crypto.setContentforLLM(buildContentListLLM(name, jsonResponse));
        return crypto;
    }

    private List<String> buildContentListLLM(String name, JSONObject sentiments) {
        List<String> contentList = new ArrayList<>();
        String llmContent = null;
        JSONArray feed = sentiments.getJSONArray("feed");
        for (int i = 0; i < feed.length(); i++) {
            if (!ObjectUtils.isEmpty(feed)){
                llmContent = this.llmContent.prepareNewsLLMContent(name, feed, i);
            }
            contentList.add(llmContent);
        }
        return contentList;
    }
}