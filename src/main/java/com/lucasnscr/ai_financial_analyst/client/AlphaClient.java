package com.lucasnscr.ai_financial_analyst.client;

import com.lucasnscr.ai_financial_analyst.converter.CryptoConverter;
import com.lucasnscr.ai_financial_analyst.converter.StockConverter;
import com.lucasnscr.ai_financial_analyst.exception.AlphaClientException;
import com.lucasnscr.ai_financial_analyst.model.Crypto;
import com.lucasnscr.ai_financial_analyst.model.Stock;
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
    private final StockConverter stockConverter;
    private final CryptoConverter cryptoConverter;
    private final String apikey;

    @Autowired
    public AlphaClient(WebClient webClient,
                       StockConverter stockConverter,
                       CryptoConverter cryptoConverter,
                       @Value("${Alpha.api-key}") String apikey) {
        this.webClient = webClient;
        this.stockConverter = stockConverter;
        this.cryptoConverter = cryptoConverter;
        this.apikey = apikey;
    }

    public Stock requestStock(String ticker) {
        return requestData(ticker, stockConverter::convertJsonToStock);
    }

    public Crypto requestCrypto(String ticker) {
        return requestData(ticker, cryptoConverter::convertJsonToCrypto);
    }

    private <T> T requestData(String ticker, BiFunction<String, JSONObject, T> converter) {
        Map<String, Object> financialJson = null;
        try {
            log.info("Request AlphaClient at {}", Instant.now());
            financialJson = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/query")
                            .queryParam("function", "NEWS_SENTIMENT")
                            .queryParam("tickers", ticker)
                            .queryParam("apikey", apikey)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            log.info("Retrieve AlphaClient at {}", Instant.now());
        } catch (Exception e) {
            throw new AlphaClientException("Error fetching data from Alpha API", e);
        }

        if (ObjectUtils.isEmpty(financialJson)) {
            log.warn("Received empty response for ticker: {}", ticker);
            return null;
        }

        return converter.apply(ticker, new JSONObject(financialJson));
    }



}