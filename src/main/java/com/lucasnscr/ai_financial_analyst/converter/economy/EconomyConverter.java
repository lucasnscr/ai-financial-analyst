package com.lucasnscr.ai_financial_analyst.converter.economy;

import com.lucasnscr.ai_financial_analyst.formatter.economy.EconomyFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.economy.*;
import com.lucasnscr.ai_financial_analyst.model.economy.EconomyData;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

@Component
public class EconomyConverter {

    private static final String REAL_GDP = "REAL_GDP"; // US PIB
    private static final String TREASURY_YIELD = "TREASURY_YIELD";
    private static final String FEDERAL_FUNDS_RATE = "FEDERAL_FUNDS_RATE";
    private static final String UNEMPLOYMENT = "UNEMPLOYMENT";
    private static final String INFLATION = "INFLATION";

    private final EconomyFormatter economyFormatter;

    @Autowired
    public EconomyConverter(EconomyFormatter economyFormatter) {
        this.economyFormatter = economyFormatter;
    }

    public EconomyData buildEconomyData(Map<String, JSONObject> responseData) {
        if (ObjectUtils.isEmpty(responseData)) {
            return null;
        }

        EconomyLLM economyLLM = new EconomyLLM();

        responseData.forEach((key, jsonData) -> {
            switch (key) {
                case REAL_GDP -> economyLLM.setGdpYearLLMList(buildGdpYear(jsonData));
                case TREASURY_YIELD -> economyLLM.setTreasureLLMList(buildTreasure(jsonData));
                case FEDERAL_FUNDS_RATE -> economyLLM.setFederalFundsRateLLMList(buildFederalFundsRate(jsonData));
                case UNEMPLOYMENT -> economyLLM.setUnemploymentLLMList(buildUnemployment(jsonData));
                case INFLATION -> economyLLM.setInflationLLMList(buildInflation(jsonData));
                default -> throw new IllegalArgumentException("Unrecognized key: " + key);
            }
        });

        EconomyData economyData = new EconomyData();
        economyData.setName("United States of America");
        economyData.setDate(LocalDate.now().toString());

        String content = economyFormatter.formatEconomy(economyLLM);
        economyData.setContentForLLM(Collections.singletonList(content));

        return economyData;
    }

    private List<GdpYearLLM> buildGdpYear(JSONObject jsonData) {
        return buildLLM(jsonData, GdpYearLLM::new,
                (llm, date, value) -> {
                    llm.setDate(date);
                    llm.setValue(Double.parseDouble(value));
                });
    }

    private List<TreasureLLM> buildTreasure(JSONObject jsonData) {
        return buildLLM(jsonData, TreasureLLM::new,
                (llm, date, value) -> {
                    llm.setDate(date);
                    llm.setValue(Double.parseDouble(value));
                });
    }

    private List<FederalFundsRateLLM> buildFederalFundsRate(JSONObject jsonData) {
        return buildLLM(jsonData, FederalFundsRateLLM::new,
                (llm, date, value) -> {
                    llm.setDate(date);
                    llm.setValue(Double.parseDouble(value));
                });
    }

    private List<UnemploymentLLM> buildUnemployment(JSONObject jsonData) {
        return buildLLM(jsonData, UnemploymentLLM::new,
                (llm, date, value) -> {
                    llm.setDate(date);
                    llm.setValue(Double.parseDouble(value));
                });
    }

    private List<InflationLLM> buildInflation(JSONObject jsonData) {
        return buildLLM(jsonData, InflationLLM::new,
                (llm, date, value) -> {
                    llm.setDate(date);
                    llm.setValue(Double.parseDouble(value));
                });
    }

    private <T> List<T> buildLLM(JSONObject jsonData, Supplier<T> constructor, TriConsumer<T, String, String> consumer) {
        List<T> resultList = new ArrayList<>();
        JSONArray dataArray = jsonData.optJSONArray("data");

        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataPoint = dataArray.optJSONObject(i);
                if (dataPoint != null) {
                    T llm = constructor.get();
                    String date = dataPoint.optString("date");
                    String value = dataPoint.optString("value");
                    consumer.accept(llm, date, value);
                    resultList.add(llm);
                }
            }
        }

        return resultList;
    }

    @FunctionalInterface
    private interface TriConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }
}