package com.lucasnscr.ai_financial_analyst.client.technical;

import com.lucasnscr.ai_financial_analyst.converter.technical.TechnicalConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.technical.Technical;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AlphaClientTechnical {

    private static final Logger log = LoggerFactory.getLogger(AlphaClientTechnical.class);

    private final WebClient webClient;
    private final String apikey;
    private final TechnicalConverter technicalConverter;

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

    @Autowired
    public AlphaClientTechnical(WebClient webClient,
                                @Value("${Alpha.api-key}") String apikey,
                                TechnicalConverter technicalConverter) {
        this.webClient = webClient;
        this.apikey = apikey;
        this.technicalConverter = technicalConverter;
    }

    public Technical requestTechnical(String ticker) {
        List<String> apiFunctions = Arrays.asList(
                RSI, MACD, BBANDS);
        Map<String, JSONObject> responseData = new HashMap<>();
        for (String function : apiFunctions) {
            JSONObject jsonObject = requestDataFromApi(ticker, function);
            responseData.put(function, jsonObject);
        }
        return technicalConverter.buildTechnical(ticker, responseData);
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
}