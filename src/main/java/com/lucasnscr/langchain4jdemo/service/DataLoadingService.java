package com.lucasnscr.langchain4jdemo.service;

import com.lucasnscr.langchain4jdemo.repository.MongoStockRepository;
import com.lucasnscr.langchain4jdemo.converter.StockConverter;
import com.lucasnscr.langchain4jdemo.model.CompanyStock;
import com.lucasnscr.langchain4jdemo.model.Stock;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@Service
public class DataLoadingService {
    private static final Logger log = LoggerFactory.getLogger(DataLoadingService.class);

    private final VectorStore vectorStore;
    private final WebClient webClient;
    private final MongoStockRepository stockRepository;
    private final StockConverter stockConverter;

    @Value("${Polygon.client.open.close}")
    private String polygonClient;

    @Value("${Polygon.client.open.close.additional}")
    private String polygonClientAdditional;

    @Value("${Polygon.client.open.close.apikey}")
    private String apikey;

    @Autowired
    public DataLoadingService(VectorStore vectorStore,
                              MongoStockRepository stockRepository,
                              StockConverter stockConverter) {
        this.vectorStore = vectorStore;
        this.stockRepository = stockRepository;
        this.stockConverter = stockConverter;
        this.webClient = WebClient.builder().baseUrl(polygonClient).build();
    }
    @Scheduled(cron = "0 0 18 * * ?")
    public void loadData() {
        for(CompanyStock companyStock : CompanyStock.values()){

            log.info("Starting DataLoadingService executing task every day 18h.");
            JSONObject financialReport = null;
            DocumentReader documentReader = null;

            try {
                financialReport = requestFinancials(companyStock.getTicker());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                documentReader = createDocumentReader(companyStock.getTicker(), financialReport);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Stock stock = stockConverter.convertJsonToLastStock(financialReport);
            stockRepository.save(stock);

            var textSplitter = new TokenTextSplitter();
            log.info("Parsing document, splitting, creating embeddings and storing in vector store...  this will take a while.");
            this.vectorStore.accept(textSplitter.apply(documentReader.get()));
            log.info("Done parsing document, splitting, creating embeddings and storing in vector store");
        }
    }

    private JSONObject requestFinancials(String ticker) {
        Map<String, Object> financialJson = null;
        LocalDate date = LocalDate.now();
        String formattedDate = "/" + date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        try {
            log.info("Request polygonClient" + Instant.now());
            financialJson = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(polygonClient
                                    + ticker
                                    + formattedDate
                                    + polygonClientAdditional
                                    + apikey)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .block();
            log.info("Retrieve polygonClient{}", Instant.now());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return new JSONObject(financialJson);
    }

    private DocumentReader createDocumentReader(String ticker, JSONObject jsonObject) {

        String textLlm = formatStock(ticker, jsonObject);
        return  new PagePdfDocumentReader(
                textLlm,
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());
    }

    private String formatStock(String ticker, JSONObject jsonObject) {

        long volumeStock = jsonObject.getLong("v");
        double averagePriceByVolume = jsonObject.getDouble("vw");
        double openPrice = jsonObject.getDouble("o");
        double closePrice = jsonObject.getDouble("c");
        double highPrice = jsonObject.getDouble("h");
        double lowPrice = jsonObject.getDouble("l");
        long timestampUnix = jsonObject.getLong("t");
        long numberOfTransactions = jsonObject.getLong("n");

        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) timestampUnix), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateTime.format(formatter);

        return String.format("The stock ticker %s recorded a transaction on %s with the following details: "
                        + "Volume traded: %d, Average price by volume: %.2f, Opening price: %.2f, Closing price: %.2f, "
                        + "Highest price: %.2f, Lowest price: %.2f, and the number of transactions: %d.",
                ticker, formattedDate, volumeStock, averagePriceByVolume, openPrice, closePrice, highPrice, lowPrice, numberOfTransactions);
    }
}