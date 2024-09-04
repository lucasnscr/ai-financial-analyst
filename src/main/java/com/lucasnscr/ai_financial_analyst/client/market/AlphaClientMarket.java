package com.lucasnscr.ai_financial_analyst.client.market;

import com.lucasnscr.ai_financial_analyst.converter.market.MarketConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.market.Stock;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.BiFunction;

@Component
public class AlphaClientMarket {

    private static final Logger log = LoggerFactory.getLogger(AlphaClientMarket.class);

    private final WebClient webClient;
    private final MarketConverter marketConverter;
    private final String apiKey;

    @Autowired
    public AlphaClientMarket(WebClient webClient,
                             MarketConverter marketConverter,
                             @Value("${Alpha.api-key}") String apiKey) {
        this.webClient = webClient;
        this.marketConverter = marketConverter;
        this.apiKey = apiKey;
    }

    public Stock requestMarketData(String ticker) {
        log.info("Requesting market data for ticker: {}", ticker);
        return requestDataFromApi(ticker, marketConverter::convertJsonToStockData);
    }

    private <T> T requestDataFromApi(String ticker, BiFunction<String, JSONObject, T> converter) {
        try {
            Map<String, Object> responseJson = performApiRequest(ticker);
            if (ObjectUtils.isEmpty(responseJson)) {
                log.warn("Received empty response for ticker: {}", ticker);
                return null;
            }
            JSONObject jsonResponse = new JSONObject(responseJson);
            return converter.apply(ticker, jsonResponse);
        } catch (AlphaClientException e) {
            log.error("Error processing API response for ticker: {}", ticker, e);
            return null;
        }
    }

    private Map<String, Object> performApiRequest(String ticker) {
        try {
            log.debug("Initiating request to Alpha API for ticker: {}", ticker);
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/query")
                            .queryParam("function", "TIME_SERIES_DAILY_ADJUSTED")
                            .queryParam("symbol", ticker)
                            .queryParam("apikey", apiKey)
                            .build())
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("API request failed with status {} and body: {}", response.statusCode(), errorBody);
                                        return Mono.error(new RuntimeException("API request failed"));
                                    })
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("API request failed with status code {} and response: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new AlphaClientException("Failed to fetch data from Alpha API due to client error", e);
        } catch (Exception e) {
            log.error("Unexpected error occurred while fetching data for ticker: {}", ticker, e);
            throw new AlphaClientException("Failed to fetch data from Alpha API", e);
        }
    }
}