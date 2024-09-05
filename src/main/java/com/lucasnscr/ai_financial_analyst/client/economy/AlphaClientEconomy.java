package com.lucasnscr.ai_financial_analyst.client.economy;

import com.lucasnscr.ai_financial_analyst.client.AlphaClient;
import com.lucasnscr.ai_financial_analyst.converter.economy.EconomyConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.economy.EconomyData;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AlphaClientEconomy {

    private static final Logger log = LoggerFactory.getLogger(AlphaClient.class);

    private final WebClient webClient;
    private final String apikey;
    private final EconomyConverter economyConverter;

    private static final String REAL_GDP = "REAL_GDP"; //US PIB
    private static final String TREASURY_YIELD = "TREASURY_YIELD";
    private static final String FEDERAL_FUNDS_RATE = "FEDERAL_FUNDS_RATE";
    private static final String UNEMPLOYMENT = "UNEMPLOYMENT";
    private static final String INFLATION = "INFLATION";
    private static final String MONTHLY = "monthly";
    private static final String ANNUAL = "annual";

    public AlphaClientEconomy(WebClient webClient,
                              @Value("${Alpha.api-key}") String apikey,
                              EconomyConverter economyConverter) {
        this.webClient = webClient;
        this.apikey = apikey;
        this.economyConverter = economyConverter;
    }

    public EconomyData requestEconomy() {
        Map<String, JSONObject> responseData = new HashMap<>();
        List<String> apiFunctions = Arrays.asList(REAL_GDP, TREASURY_YIELD, FEDERAL_FUNDS_RATE, UNEMPLOYMENT, INFLATION);
        apiFunctions.forEach(function -> {
            String interval = determineInterval(function);
            JSONObject jsonObject = requestDataFromApi(function, interval);
            responseData.put(function, jsonObject);
        });
        return economyConverter.buildEconomyData(responseData);
    }

    private String determineInterval(String function) {
        return switch (function) {
            case REAL_GDP -> MONTHLY;
            case TREASURY_YIELD, FEDERAL_FUNDS_RATE -> ANNUAL;
            default -> null;
        };
    }

    private JSONObject requestDataFromApi(String function, String interval) {
        Map<String, Object> responseJson = performApiRequest(function, interval);
        if (ObjectUtils.isEmpty(responseJson)) {
            log.warn("Received empty response for function: {}", function);
            return new JSONObject();
        }
        return new JSONObject(responseJson);
    }

    private Map<String, Object> performApiRequest(String function, String interval) {
        try {
            log.info("Requesting data from Alpha API at {}", Instant.now());
            return this.webClient.get()
                    .uri(uriBuilder -> buildUri(uriBuilder, function, interval))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

        } catch (Exception e) {
            log.error("Error fetching data from Alpha API for function: {}", function, e);
            throw new AlphaClientException("Error fetching data from Alpha API for function: " + function, e);
        }
    }

    private URI buildUri(UriBuilder uriBuilder, String function, String interval) {
        uriBuilder.path("/query")
                .queryParam("function", function)
                .queryParam("apikey", apikey);
        if (!ObjectUtils.isEmpty(interval)) {
            uriBuilder.queryParam("interval", interval);
        }
        return uriBuilder.build();
    }
}