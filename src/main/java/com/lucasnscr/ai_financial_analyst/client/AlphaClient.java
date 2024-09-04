package com.lucasnscr.ai_financial_analyst.client;

import com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals.NewsAndSentimentalsCryptoConverter;
import com.lucasnscr.ai_financial_analyst.converter.classification.ClassificationConverter;
import com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals.NewsAndSentimentalsStockConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.classification.StockClassification;
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
import java.util.Map;
import java.util.function.BiFunction;

@Component
public class AlphaClient {

    private static final Logger log = LoggerFactory.getLogger(AlphaClient.class);

    private final WebClient webClient;
    private final NewsAndSentimentalsStockConverter newsAndSentimentalsStockConverter;
    private final NewsAndSentimentalsCryptoConverter newsAndSentimentalsCryptoConverter;
    private final ClassificationConverter classificationConverter;
    private final String apikey;

    @Autowired
    public AlphaClient(WebClient webClient,
                       NewsAndSentimentalsStockConverter newsAndSentimentalsStockConverter,
                       NewsAndSentimentalsCryptoConverter newsAndSentimentalsCryptoConverter,
                       ClassificationConverter classificationConverter,
                       @Value("${Alpha.api-key}") String apikey) {
        this.webClient = webClient;
        this.newsAndSentimentalsStockConverter = newsAndSentimentalsStockConverter;
        this.newsAndSentimentalsCryptoConverter = newsAndSentimentalsCryptoConverter;
        this.classificationConverter = classificationConverter;
        this.apikey = apikey;
    }

    public StockNewsAndSentimentals requestStock(String ticker) {
        return requestDataFromApi(
                ticker,
                "NEWS_SENTIMENT",
                newsAndSentimentalsStockConverter::convertJsonToStock);
    }

    public CryptoNewsAndSentimentals requestCrypto(String ticker) {
        return requestDataFromApi(
                ticker,
                "NEWS_SENTIMENT",
                newsAndSentimentalsCryptoConverter::convertJsonToCrypto);
    }

    public StockClassification requestGainersLosers() {
        return requestDataFromApi(
                null,
                "TOP_GAINERS_LOSERS",
                (ignored, jsonResponse) -> classificationConverter.convertJsonToStockClassification(jsonResponse));
    }

    private <T> T requestDataFromApi(String ticker, String function, BiFunction<String, JSONObject, T> converter) {
        Map<String, Object> responseJson = performApiRequest(ticker, function);
        if (ObjectUtils.isEmpty(responseJson)) {
            log.warn("Received empty response for ticker: {}", ticker);
            return null;
        }
        return converter.apply(ticker, new JSONObject(responseJson));
    }

    private Map<String, Object> performApiRequest(String ticker, String function) {
        try {
            log.info("Requesting data from Alpha API at {}", Instant.now());
            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/query")
                            .queryParam("function", function)
                            .queryParam("tickers", ticker)
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