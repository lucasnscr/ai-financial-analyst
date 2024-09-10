package com.lucasnscr.ai_financial_analyst.converter.technical;

import com.lucasnscr.ai_financial_analyst.formatter.technical.TechnicalFormatter;
import com.lucasnscr.ai_financial_analyst.llm.model.technical.BbandsLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.technical.MacdLLM;
import com.lucasnscr.ai_financial_analyst.llm.model.technical.TechnicalDataLLM;
import com.lucasnscr.ai_financial_analyst.model.technical.Technical;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;

@Component
public class TechnicalConverter {

    private static final String RSI = "RSI";
    private static final String MACD = "MACD";
    private static final String BBANDS = "BBANDS";

    private final TechnicalFormatter economyFormatter;

    @Autowired
    public TechnicalConverter(TechnicalFormatter economyFormatter) {
        this.economyFormatter = economyFormatter;
    }

    public Technical buildTechnical(String name, Map<String, JSONObject> responseData) {
        if (ObjectUtils.isEmpty(responseData)) {
            return null;
        }
        TechnicalDataLLM technicalDataLLM = new TechnicalDataLLM();
        responseData.forEach((key, jsonData) -> {
            switch (key) {
                case RSI -> technicalDataLLM.setRsiList(buildRsi(jsonData));
                case MACD -> technicalDataLLM.setMacdLLMList(buildMacd(jsonData));
                case BBANDS -> technicalDataLLM.setBbandsLLMList(buildBbands(jsonData));
                default -> throw new IllegalArgumentException("Unrecognized key: " + key);
            }
        });
        Technical technical = new Technical();
        technical.setName(name);
        technical.setDate(LocalDate.now().toString());
        String content = economyFormatter.formatTechnical(name, technicalDataLLM);
        technical.setContentForLLM(Collections.singletonList(content));
        return technical;
    }

    private List<BbandsLLM> buildBbands(JSONObject jsonData) {
        List<BbandsLLM> bbandsList = new ArrayList<>();
        Iterator<String> keys = jsonData.keys();
        while (keys.hasNext()) {
            String date = keys.next();
            JSONObject bbandsData = jsonData.optJSONObject(date);
            if (bbandsData != null) {
                double realUpperBand = bbandsData.optDouble("Real Upper Band", 0.0);
                double realMiddleBand = bbandsData.optDouble("Real Middle Band", 0.0);
                double realLowerBand = bbandsData.optDouble("Real Lower Band", 0.0);
                BbandsLLM bbandsEntry = new BbandsLLM();
                bbandsEntry.setDate(date);
                bbandsEntry.setRealUpperBand(realUpperBand);
                bbandsEntry.setRealMiddleBand(realMiddleBand);
                bbandsEntry.setRealLowerBand(realLowerBand);
                bbandsList.add(bbandsEntry);
            }
        }

        return bbandsList;
    }

    private List<MacdLLM> buildMacd(JSONObject jsonData) {
        List<MacdLLM> macdList = new ArrayList<>();
        Iterator<String> keys = jsonData.keys();
        while (keys.hasNext()) {
            String date = keys.next();
            JSONObject macdData = jsonData.optJSONObject(date);
            if (macdData != null) {
                double macd = macdData.optDouble("MACD", 0.0);
                double macdSignal = macdData.optDouble("MACD_Signal", 0.0);
                double macdHist = macdData.optDouble("MACD_Hist", 0.0);
                MacdLLM macdEntry = new MacdLLM();
                macdEntry.setDate(date);
                macdEntry.setMacd(macd);
                macdEntry.setMacdSignal(macdSignal);
                macdEntry.setMacdHist(macdHist);
                macdList.add(macdEntry);
            }
        }
        return macdList;
    }

    private List<Double> buildRsi(JSONObject jsonData) {
        List<Double> rsiValues = new ArrayList<>();
        Iterator<String> keys = jsonData.keys();
        while (keys.hasNext()) {
            String date = keys.next();
            JSONObject rsiData = jsonData.optJSONObject(date);
            if (rsiData != null) {
                double rsiValue = rsiData.optDouble("RSI", 0.0);
                rsiValues.add(rsiValue);
            }
        }
        return rsiValues;
    }
}