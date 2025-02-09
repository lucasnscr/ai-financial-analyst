package com.lucasnscr.ai_financial_analyst.client.classification;

import com.lucasnscr.ai_financial_analyst.converter.classification.ClassificationConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.classification.StockClassification;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class AlphaClientClassification {

    private static final Logger log = LoggerFactory.getLogger(AlphaClientClassification.class);

    private final WebClient webClient;
    private final ClassificationConverter classificationConverter;
    private final String apikey;

    public AlphaClientClassification(WebClient webClient,
                                     ClassificationConverter classificationConverter,
                                     @Value("${Alpha.api-key}") String apikey) {
        this.webClient = webClient;
        this.classificationConverter = classificationConverter;
        this.apikey = apikey;
    }

    public StockClassification requestGainersLosers() {
        return requestDataFromApi(
                (ignored, jsonResponse) -> classificationConverter.convertJsonToStockClassification(jsonResponse)
        );
    }

    private <T> T requestDataFromApi(BiFunction<String, JSONObject, T> converter) {
        Map<String, Object> responseJson = performApiRequest();
        if (ObjectUtils.isEmpty(responseJson)) {
            log.warn("Received empty response for ticker: {}", (String) null);
            return null;
        }
        return converter.apply(null, new JSONObject(responseJson));
    }

    private Map<String, Object> performApiRequest() {
        try {
            log.info("Requesting data from Alpha API at {}", Instant.now());
            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/query")
                            .queryParam("function", "TOP_GAINERS_LOSERS")
                            .queryParam("tickers", (String) null)
                            .queryParam("apikey", apikey)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error fetching data from Alpha API for ticker: {}", null, e);
            throw new AlphaClientException("Error fetching data from Alpha API", e);
        }
    }
}
