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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class EconomyConverter {

    private static final String REAL_GDP = "REAL_GDP"; //US PIB
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
        EconomyData economyData = null;
        String content = "";
        if (!ObjectUtils.isEmpty(responseData)) {
            economyData = new EconomyData();
            EconomyLLM economyLLM = new EconomyLLM();
            List<GdpYearLLM> gdpYearLLMList;
            List<TreasureLLM> treasureLLMList;
            List<FederalFundsRateLLM> federalFundsRateLLMList;
            List<UnemploymentLLM> unemploymentLLMList;
            List<InflationLLM> inflationLLMList;
            for (Map.Entry<String, JSONObject> entry : responseData.entrySet()) {
                String key = entry.getKey();
                JSONObject jsonData = entry.getValue();

                switch (key) {
                    case REAL_GDP:
                        gdpYearLLMList = buildGdpYear(jsonData);
                        economyLLM.setGdpYearLLMList(gdpYearLLMList);
                        break;
                    case TREASURY_YIELD:
                        treasureLLMList = buildTreasure(jsonData);
                        economyLLM.setTreasureLLMList(treasureLLMList);
                        break;
                    case FEDERAL_FUNDS_RATE:
                        federalFundsRateLLMList = buildFederalFundsRate(jsonData);
                        economyLLM.setFederalFundsRateLLMList(federalFundsRateLLMList);
                        break;
                    case UNEMPLOYMENT:
                        unemploymentLLMList = buildUnemployment(jsonData);
                        economyLLM.setUnemploymentLLMList(unemploymentLLMList);
                        break;
                    case INFLATION:
                        inflationLLMList = buildInflation(jsonData);
                        economyLLM.setInflationLLMList(inflationLLMList);
                        break;
                    default:
                        break;
                }
            }
            if (!ObjectUtils.isEmpty(economyLLM)) {
                content = economyFormatter.formatEconomy(economyLLM);
            }
            economyData.setName("United States of America");
            economyData.setDate(LocalDate.now().toString());
            economyData.setContentForLLM(Collections.singletonList(content));
        }
        return economyData;
    }

    private List<GdpYearLLM> buildGdpYear(JSONObject jsonData) {
        List<GdpYearLLM> gdpYearList = new ArrayList<>();
        JSONArray dataArray = jsonData.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataPoint = dataArray.optJSONObject(i);
                if (dataPoint != null) {
                    GdpYearLLM gdpYear = new GdpYearLLM();
                    gdpYear.setDate(dataPoint.optString("date"));
                    gdpYear.setValue(dataPoint.optDouble("value"));
                    gdpYearList.add(gdpYear);
                }
            }
        }
        return gdpYearList;
    }

    private List<TreasureLLM> buildTreasure(JSONObject jsonData) {
        List<TreasureLLM> treasureList = new ArrayList<>();
        JSONArray dataArray = jsonData.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject treasureData = dataArray.optJSONObject(i);
                if (treasureData != null) {
                    TreasureLLM treasure = new TreasureLLM();
                    treasure.setDate(treasureData.optString("date"));
                    treasure.setValue(treasureData.optDouble("value"));
                    treasureList.add(treasure);
                }
            }
        }
        return treasureList;
    }

    private List<FederalFundsRateLLM> buildFederalFundsRate(JSONObject jsonData) {
        List<FederalFundsRateLLM> federalFundsRateList = new ArrayList<>();
        JSONArray dataArray = jsonData.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataPoint = dataArray.optJSONObject(i);
                if (dataPoint != null) {
                    FederalFundsRateLLM federalFundsRate = new FederalFundsRateLLM();
                    federalFundsRate.setDate(dataPoint.optString("date"));
                    federalFundsRate.setValue(dataPoint.optDouble("value"));
                    federalFundsRateList.add(federalFundsRate);
                }
            }
        }
        return federalFundsRateList;
    }

    private List<UnemploymentLLM> buildUnemployment(JSONObject jsonData) {
        List<UnemploymentLLM> unemploymentList = new ArrayList<>();
        JSONArray dataArray = jsonData.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataPoint = dataArray.optJSONObject(i);
                if (dataPoint != null) {
                    UnemploymentLLM unemployment = new UnemploymentLLM();
                    unemployment.setDate(dataPoint.optString("date"));
                    unemployment.setValue(dataPoint.optDouble("value"));
                    unemploymentList.add(unemployment);
                }
            }
        }
        return unemploymentList;
    }

    private List<InflationLLM> buildInflation(JSONObject jsonData) {
        List<InflationLLM> inflationList = new ArrayList<>();
        JSONArray dataArray = jsonData.optJSONArray("data");
        if (dataArray != null) {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataPoint = dataArray.optJSONObject(i);
                if (dataPoint != null) {
                    InflationLLM inflation = new InflationLLM();
                    inflation.setDate(dataPoint.optString("date"));
                    inflation.setValue(dataPoint.optDouble("value"));
                    inflationList.add(inflation);
                }
            }
        }
        return inflationList;
    }
}