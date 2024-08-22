package com.lucasnscr.ai_financial_analyst.service;

import com.lucasnscr.ai_financial_analyst.client.AlphaClient;
import com.lucasnscr.ai_financial_analyst.enums.CryptoEnum;
import com.lucasnscr.ai_financial_analyst.enums.StockEnum;
import com.lucasnscr.ai_financial_analyst.model.Crypto;
import com.lucasnscr.ai_financial_analyst.model.Stock;
import com.lucasnscr.ai_financial_analyst.model.StockClassification;
import com.lucasnscr.ai_financial_analyst.repository.MongoCryptoRepository;
import com.lucasnscr.ai_financial_analyst.repository.MongoStockClassificationRepository;
import com.lucasnscr.ai_financial_analyst.repository.MongoStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class DataLoadingService {

    private static final Logger log = LoggerFactory.getLogger(DataLoadingService.class);

    private final VectorStore vectorStore;
    private final MongoStockRepository stockRepository;
    private final MongoCryptoRepository cryptoRepository;
    private final MongoStockClassificationRepository stockClassificationRepository;
    private final AlphaClient alphaClient;

    @Autowired
    public DataLoadingService(@Qualifier("vectorStoreDB") VectorStore vectorStore,
                              MongoStockRepository stockRepository,
                              MongoCryptoRepository cryptoRepository,
                              MongoStockClassificationRepository stockClassificationRepository,
                              AlphaClient alphaClient) {
        this.vectorStore = vectorStore;
        this.stockRepository = stockRepository;
        this.cryptoRepository = cryptoRepository;
        this.stockClassificationRepository = stockClassificationRepository;
        this.alphaClient = alphaClient;
    }

    public void loadData() {
        log.info("Starting DataLoadingService.");

        handleStockClassification();

        processEntities(stockRepository, StockEnum.values(), this::processStock);
        processEntities(cryptoRepository, CryptoEnum.values(), this::processCrypto);

        log.info("DataLoadingService completed.");
    }

    private void handleStockClassification() {
        StockClassification stockClassification = alphaClient.requestGainersLosers();
        if (ObjectUtils.isEmpty(stockClassification)) {
            return;
        }

        stockClassificationRepository.save(stockClassification);
        saveVectorDb(stockClassification.getClassifications());
    }

    private <T, E extends Enum<E>> void processEntities(MongoRepository<T, String> repository, E[] enumValues, Consumer<E> processor) {
        List<T> entities = repository.findAll();
        if (CollectionUtils.isEmpty(entities)) {
            loadEntities(enumValues, processor);
        } else {
            entities.stream()
                    .map(this::getContentForLLM)
                    .filter(content -> !CollectionUtils.isEmpty(content))
                    .forEach(this::saveVectorDb);
        }
    }

    private <T extends Enum<T>> void loadEntities(T[] enumValues, Consumer<T> processor) {
        for (T enumValue : enumValues) {
            try {
                processor.accept(enumValue);
            } catch (Exception e) {
                log.error("Error processing entity: {}", enumValue, e);
            }
        }
    }

    private void processStock(StockEnum stockEnum) {
        try {
            Stock stock = alphaClient.requestStock(stockEnum.getTicker());
            processEntity(stock, stockRepository::save);
        } catch (Exception e) {
            log.error("Error processing stock: {}", stockEnum.getTicker(), e);
        }
    }

    private void processCrypto(CryptoEnum cryptoEnum) {
        try {
            Crypto crypto = alphaClient.requestCrypto(cryptoEnum.getTicker());
            processEntity(crypto, cryptoRepository::save);
        } catch (Exception e) {
            log.error("Error processing crypto: {}", cryptoEnum.getTicker(), e);
        }
    }

    private <T> void processEntity(T entity, Consumer<T> saveFunction) {
        if (ObjectUtils.isEmpty(entity)) {
            return;
        }
        saveFunction.accept(entity);
        saveVectorDb(getContentForLLM(entity));
    }

    private List<String> getContentForLLM(Object entity) {
        if (entity instanceof Stock) {
            return ((Stock) entity).getContentforLLM();
        } else if (entity instanceof Crypto) {
            return ((Crypto) entity).getContentforLLM();
        }
        return Collections.emptyList();
    }

    private void saveVectorDb(List<String> contentList) {
        if (CollectionUtils.isEmpty(contentList)) {
            return;
        }
        List<Document> documents = contentList.stream()
                .map(Document::new)
                .collect(Collectors.toList());
        log.info("Creating embeddings and storing in vector store...");
        vectorStore.add(documents);
        log.info("Done storing embeddings in vector store.");
    }
}