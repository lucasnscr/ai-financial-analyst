package com.lucasnscr.ai_financial_analyst.client.technical;

import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.technical.Technical;
import com.nimbusds.jose.shaded.gson.Gson;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Component
public class AlphaClientTechnical {

    private static final Logger log = LoggerFactory.getLogger(AlphaClientTechnical.class);

    private final WebClient webClient;
    private final String apikey;

    private static final String RSI = "RSI";
    private static final String MACD = "MACD";
    private static final String BBANDS = "BBANDS";
    private static final String WEEKLY = "weekly";
    private static final String DAILY = "daily";
    private static final String TEN = "10";
    private static final String FIVE = "5";
    private static final String THREE = "3";
    private static final String OPEN = "open";
    private static final String CLOSE = "close";

    public AlphaClientTechnical(WebClient webClient,
                                @Value("${Alpha.api-key}") String apikey) {
        this.webClient = webClient;
        this.apikey = apikey;
    }

    public Technical requestTechnical(String ticker) {
        List<String> apiFunctions = Arrays.asList(
                RSI, MACD, BBANDS);
        Map<String, JSONObject> responseData = new HashMap<>();
        for (String function : apiFunctions) {
            JSONObject jsonObject = requestDataFromApi(ticker, function);
            responseData.put(function, jsonObject);
        }


        Technical technical = new Technical();
        technical.setName(ticker);
        technical.setDate(LocalDate.now().toString());
        technical.setContentForLLM(parseJsonToStringList(responseData));
        return technical;
//        return technicalConverter.buildTechnical(ticker, responseData);
    }

    private JSONObject requestDataFromApi(String ticker, String function) {
        Map<String, Object> responseJson = performApiRequest(ticker, function);
        if (ObjectUtils.isEmpty(responseJson)) {
            log.warn("Received empty response for ticker: {}", ticker);
            return null;
        }
        return new JSONObject(responseJson);
    }

    private Map<String, Object> performApiRequest(String ticker, String function) {
        try {
            log.info("Requesting market data for ticker: {}", ticker);
            log.info("Requesting data from Alpha API at {}", Instant.now());
            if (RSI.equals(function)) {
                return webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/query")
                                .queryParam("function", function)
                                .queryParam("symbol", ticker)
                                .queryParam("interval", WEEKLY)
                                .queryParam("time_period", TEN)
                                .queryParam("series_type", OPEN)
                                .queryParam("apikey", apikey)
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .block();
            } else if (MACD.equals(function)) {
                return webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/query")
                                .queryParam("function", function)
                                .queryParam("symbol", ticker)
                                .queryParam("interval", DAILY)
                                .queryParam("series_type", OPEN)
                                .queryParam("apikey", apikey)
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .block();

            } else if (BBANDS.equals(function)) {
                return webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/query")
                                .queryParam("function", function)
                                .queryParam("symbol", ticker)
                                .queryParam("interval", WEEKLY)
                                .queryParam("time_period", FIVE)
                                .queryParam("series_type", CLOSE)
                                .queryParam("nbdevup", THREE)
                                .queryParam("nbdevdn", THREE)
                                .queryParam("apikey", apikey)
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .block();

            }
        } catch (Exception e) {
            log.error("Error fetching data from Alpha API for ticker: {}", ticker, e);
            throw new AlphaClientException("Error fetching data from Alpha API", e);
        }
        return Map.of();
    }

    private List<String> parseJsonToStringList(Map<String, JSONObject> responseData){


        String macdContent = "";
        String rsiContent = "";
        String bBandContent = "";
        List<String> jsonStringList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(responseData)){
            Gson gson = new Gson();
            JSONObject macd = responseData.get("MACD");
            if (!ObjectUtils.isEmpty(macd)){
                JSONObject technicalDataMacd = macd.getJSONObject("Technical Analysis: MACD");
                macdContent = gson.toJson(technicalDataMacd);
            }

            JSONObject rsi = responseData.get("RSI");
            if (!ObjectUtils.isEmpty(rsi)){
                JSONObject technicalDataRsi = rsi.getJSONObject("Technical Analysis: RSI");
                rsiContent = gson.toJson(technicalDataRsi);
            }

            JSONObject bBand = responseData.get("BBANDS");
            if (!ObjectUtils.isEmpty(bBand)){
                JSONObject technicalDataBband = bBand.getJSONObject("Technical Analysis: BBANDS");
                bBandContent = gson.toJson(technicalDataBband);
            }
            jsonStringList.add(macdContent);
            jsonStringList.add(rsiContent);
            jsonStringList.add(bBandContent);
        }
        return jsonStringList;
    }

}