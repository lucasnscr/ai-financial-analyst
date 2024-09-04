package com.lucasnscr.ai_financial_analyst.converter.classification;

import com.lucasnscr.ai_financial_analyst.llm.LLMContent;
import com.lucasnscr.ai_financial_analyst.model.classification.StockClassification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class ClassificationConverter {

    private static final String TOP_GAINERS = "top_gainers";
    private static final String TOP_LOSERS = "top_losers";
    private static final String MOST_ACTIVELY_TRADED = "most_actively_traded";

    private final LLMContent llmContent;

    public ClassificationConverter(LLMContent llmContent) {
        this.llmContent = llmContent;
    }

    public StockClassification convertJsonToStockClassification(JSONObject jsonResponse) {
        if (ObjectUtils.isEmpty(jsonResponse)) {
            return new StockClassification();
        }

        return new StockClassification(jsonResponse.getString("last_updated"),
                null,
                buildStockClassification(jsonResponse)
        );
    }

    private List<String> buildStockClassification(JSONObject classification) {
        List<String> classifications = new ArrayList<>();
        Stream.of(
                new ClassificationData(TOP_GAINERS, classification.optJSONArray(TOP_GAINERS)),
                new ClassificationData(TOP_LOSERS, classification.optJSONArray(TOP_LOSERS)),
                new ClassificationData(MOST_ACTIVELY_TRADED, classification.optJSONArray(MOST_ACTIVELY_TRADED))
        ).forEach(data -> addClassificationData(classifications, data));
        return classifications;
    }

    private void addClassificationData(List<String> classifications, ClassificationData data) {
        if (ObjectUtils.isEmpty(data.jsonArray)) {
            return;
        }
        for (int i = 0; i < data.jsonArray.length(); i++) {
            classifications.add(llmContent.prepareClassification(data.type, data.jsonArray, i));
        }
    }

    private static class ClassificationData {
        private final String type;
        private final JSONArray jsonArray;

        public ClassificationData(String type, JSONArray jsonArray) {
            this.type = type;
            this.jsonArray = jsonArray;
        }
    }
}
