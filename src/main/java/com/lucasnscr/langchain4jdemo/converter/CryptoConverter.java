package com.lucasnscr.langchain4jdemo.converter;

import com.lucasnscr.langchain4jdemo.llm.LLMContent;
import com.lucasnscr.langchain4jdemo.model.Crypto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        for (int i = 0; i < sentiments.length(); i++) {
            JSONArray feed = sentiments.getJSONArray("feed");
            String llmContent = this.llmContent.prepareLLMContent(name, feed, i);
            contentList.add(llmContent);
        }
        return contentList;
    }
}
