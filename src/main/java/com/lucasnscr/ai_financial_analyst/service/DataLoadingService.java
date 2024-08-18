package com.lucasnscr.ai_financial_analyst.service;

import com.lucasnscr.ai_financial_analyst.client.AlphaClient;
import com.lucasnscr.ai_financial_analyst.model.Crypto;
import com.lucasnscr.ai_financial_analyst.model.CryptoEnum;
import com.lucasnscr.ai_financial_analyst.model.Stock;
import com.lucasnscr.ai_financial_analyst.model.StockEnum;
import com.lucasnscr.ai_financial_analyst.repository.MongoCryptoRepository;
import com.lucasnscr.ai_financial_analyst.repository.MongoStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


@Service
public class DataLoadingService {

    private static final Logger log = LoggerFactory.getLogger(DataLoadingService.class);

    private final VectorStore vectorStore;
    private final MongoStockRepository stockRepository;
    private final MongoCryptoRepository cryptoRepository;
    private final AlphaClient alphaClient;

    @Autowired
    public DataLoadingService(@Qualifier("vectorStoreDB") VectorStore vectorStore,
                              MongoStockRepository stockRepository,
                              MongoCryptoRepository cryptoRepository,
                              AlphaClient alphaClient) {
        this.vectorStore = vectorStore;
        this.stockRepository = stockRepository;
        this.cryptoRepository = cryptoRepository;
        this.alphaClient = alphaClient;
    }
//    @Scheduled(cron = "0 0 18 * * ?")
    public void loadData() {
        log.info("Starting DataLoadingService.");
        loadEntities(StockEnum.values(), this::processStock);
        loadEntities(CryptoEnum.values(), this::processCrypto);
    }

    private <T extends Enum<T>> void loadEntities(T[] enumValues, Consumer<T> processor) {
        for (T enumValue : enumValues) {
            try {
                processor.accept(enumValue);
            } catch (Exception e) {
                log.error("Error processing entity: {}", enumValue, e);
                throw new RuntimeException(e);
            }
        }
    }

    private void processStock(StockEnum stockEnum) {
        try {
            Stock stock = alphaClient.requestStock(stockEnum.getTicker());
            if (!ObjectUtils.isEmpty(stock)) {
                stockRepository.save(stock);
                saveVectorDb(stock.getContentforLLM());
            }
        } catch (Exception e) {
            log.error("Error processing stock: " + stockEnum.getTicker(), e);
            throw new RuntimeException(e);
        }
    }

    private void processCrypto(CryptoEnum cryptoEnum) {
        try {
            Crypto crypto = alphaClient.requestCrypto(cryptoEnum.getTicker());
            if (!ObjectUtils.isEmpty(crypto)) {
                cryptoRepository.save(crypto);
                saveVectorDb(crypto.getContentforLLM());
            }
        } catch (Exception e) {
            log.error("Error processing crypto: " + cryptoEnum.getTicker(), e);
            throw new RuntimeException(e);
        }
    }

    private void saveVectorDb(List<String> contentList) {
        List<Document> documents = new ArrayList<>();
        for (String content : contentList){
            log.info("Creating embeddings and storing in vector store...  this will take a while.");
            documents.add(new Document(content));
        }
        this.vectorStore.add(documents);
        log.info("Done parsing document, splitting, creating embeddings and storing in vector store");
    }
}