package com.lucasnscr.langchain4jdemo.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucasnscr.langchain4jdemo.exception.StockNotAvailableException;
import com.lucasnscr.langchain4jdemo.validations.StockAnalyzer;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class DataLoadingService {
    private static final Logger log = LoggerFactory.getLogger(DataLoadingService.class);

    private EmbeddingStore embeddingStore;
    private final WebClient webClient;
    private StockAnalyzer stockAnalyzer;

    @Value("${Polygon.client.open.close}")
    private String polygonClient;

    @Value("${Polygon.client.open.close.additional}")
    private String polygonClientAdditional;

    @Value("${Polygon.client.open.close.apikey}")
    private String apikey;

    @Autowired
    public DataLoadingService(EmbeddingStore embeddingStore,
                              WebClient.Builder webClient,
                              StockAnalyzer stockAnalyzer) {
        this.embeddingStore = embeddingStore;
        this.stockAnalyzer = stockAnalyzer;
        this.webClient = WebClient.builder().baseUrl(polygonClient).build();

    }

    public void loadData(String ticker){

        List<String> strings = null;
        try {
            strings = requestFinancials(ticker);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (CollectionUtils.isEmpty(strings)){
            throw new StockNotAvailableException(HttpStatus.INTERNAL_SERVER_ERROR, "Convert Map on list failed");
        }

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        TextSegment segment1 = TextSegment.from(String.valueOf(strings));
        Embedding embedding1 = embeddingModel.embed(segment1).content();
        embeddingStore.add(embedding1, segment1);

    }

    private List<String> requestFinancials(String ticker) throws Exception {
        try {
            stockAnalyzer.analyzeStock(ticker);
        }catch (Exception e){
            throw new StockNotAvailableException(HttpStatus.NOT_FOUND, "Stock not available for analysis.");
        }

        LocalDate date = LocalDate.now();
        String formattedDate = "/" + date.format(DateTimeFormatter.ISO_LOCAL_DATE);

        String financialJson = this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(polygonClient
                        + ticker
                        + formattedDate
                        + polygonClientAdditional
                        + apikey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.trace("Json: {}", financialJson);
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        JsonParser parser = factory.createParser(financialJson);
        JsonNode root = mapper.readTree(parser);
        Map map = mapper.treeToValue(root, Map.class);
        return convert(map);
    }

    private List<String> convert(Map<String, Object> map) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String valueString = (value != null) ? value.toString() : "null";
            result.add(key + "=" + valueString);
        }
        return result;
    }
}
