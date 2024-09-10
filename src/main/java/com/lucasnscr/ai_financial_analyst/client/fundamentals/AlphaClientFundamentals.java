package com.lucasnscr.ai_financial_analyst.client.fundamentals;

import com.lucasnscr.ai_financial_analyst.converter.fundamentals.FundamentalsConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.fundamentals.FundamentalsDataCompany;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.*;

@Component
public class AlphaClientFundamentals {

    private static final Logger log = LoggerFactory.getLogger(AlphaClientFundamentals.class);

    private final WebClient webClient;
    private final String apikey;
    private final FundamentalsConverter fundamentalsConverter;

    private static final String OVERVIEW = "OVERVIEW";
    private static final String DIVIDENDS = "DIVIDENDS";
    private static final String SPLITS = "SPLITS";
    private static final String INCOME_STATEMENT = "INCOME_STATEMENT";
    private static final String BALANCE_SHEET = "BALANCE_SHEET";
    private static final String CASH_FLOW = "CASH_FLOW";
    private static final String EARNINGS = "EARNINGS";

    public AlphaClientFundamentals(WebClient webClient,
                                   @Value("${Alpha.api-key}") String apikey,
                                   FundamentalsConverter fundamentalsConverter) {
        this.webClient = webClient;
        this.apikey = apikey;
        this.fundamentalsConverter = fundamentalsConverter;
    }

    public FundamentalsDataCompany requestFundamentalsData(String ticker) {
        List<String> apiFunctions = Arrays.asList(
                OVERVIEW, DIVIDENDS, SPLITS,
                INCOME_STATEMENT, BALANCE_SHEET,
                CASH_FLOW, EARNINGS);
        Map<String, JSONObject> responseData = new HashMap<>();
        for (String function : apiFunctions) {
            JSONObject jsonObject = requestDataFromApi(ticker, function);
            responseData.put(function, jsonObject);
        }
        return fundamentalsConverter.buildFundamentalsDataCompany(ticker, responseData);
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
            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/query")
                            .queryParam("function", function)
                            .queryParam("symbol", ticker)
                            .queryParam("apikey", apikey)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error fetching data from Alpha API for ticker: {}", ticker, e);
            throw new AlphaClientException("Error fetching data from Alpha API", e);
        }
    }
}
