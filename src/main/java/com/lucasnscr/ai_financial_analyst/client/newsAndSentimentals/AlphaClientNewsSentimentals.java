package com.lucasnscr.ai_financial_analyst.client.newsAndSentimentals;

import com.lucasnscr.ai_financial_analyst.converter.newsAndSentimentals.NewsAndSentimentalsConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
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
public class AlphaClientNewsSentimentals {

    private static final Logger log = LoggerFactory.getLogger(AlphaClientNewsSentimentals.class);

    private final WebClient webClient;
    private final NewsAndSentimentalsConverter newsAndSentimentalsConverter;
    private final String apikey;

    @Autowired
    public AlphaClientNewsSentimentals(WebClient webClient,
                       NewsAndSentimentalsConverter newsAndSentimentalsConverter,
                       @Value("${Alpha.api-key}") String apikey) {
        this.webClient = webClient;
        this.newsAndSentimentalsConverter = newsAndSentimentalsConverter;
        this.apikey = apikey;
    }

    public StockNewsAndSentimentals requestStock(String ticker) {
        return requestDataFromApi(
                ticker,
                newsAndSentimentalsConverter::convertJsonToStock
        );
    }

    public CryptoNewsAndSentimentals requestCrypto(String ticker) {
        return requestDataFromApi(
                ticker,
                newsAndSentimentalsConverter::convertJsonToCrypto
        );
    }

    private <T> T requestDataFromApi(String ticker, BiFunction<String, JSONObject, T> converter) {
        Map<String, Object> responseJson = performApiRequest(ticker);
        if (ObjectUtils.isEmpty(responseJson)) {
            log.warn("Received empty response for ticker: {}", ticker);
            return null;
        }
        return converter.apply(ticker, new JSONObject(responseJson));
    }

    private Map<String, Object> performApiRequest(String ticker) {
        try {
            log.info("Requesting market data for ticker: {}", ticker);
            log.info("Requesting data from Alpha API at {}", Instant.now());
            return this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/query")
                            .queryParam("function", "NEWS_SENTIMENT")
                            .queryParam("tickers", ticker)
                            .queryParam("apikey", apikey)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();
        } catch (Exception e) {
            log.error("Error fetching data from Alpha API for ticker: {}", ticker, e);
            throw new AlphaClientException("Error fetching data from Alpha API", e);
        }
    }

}
