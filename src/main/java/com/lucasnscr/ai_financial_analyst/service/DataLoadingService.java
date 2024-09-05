package com.lucasnscr.ai_financial_analyst.service;

import com.lucasnscr.ai_financial_analyst.client.AlphaClient;
import com.lucasnscr.ai_financial_analyst.client.economy.AlphaClientEconomy;
import com.lucasnscr.ai_financial_analyst.client.fundamentals.AlphaClientFundamentals;
import com.lucasnscr.ai_financial_analyst.client.market.AlphaClientMarket;
import com.lucasnscr.ai_financial_analyst.enums.CryptoEnum;
import com.lucasnscr.ai_financial_analyst.enums.StockEnum;
import com.lucasnscr.ai_financial_analyst.model.economy.EconomyData;
import com.lucasnscr.ai_financial_analyst.model.fundamentals.FundamentalsDataCompany;
import com.lucasnscr.ai_financial_analyst.model.market.Stock;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.classification.StockClassification;
import com.lucasnscr.ai_financial_analyst.repository.*;
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
    private final StockNewsAndSentimentalsRepository stockRepository;
    private final CryptoNewsAndSentimentalsRepository cryptoRepository;
    private final StockClassificationRepository stockClassificationRepository;
    private final StockMarketDataRepository stockMarketDataRepository;
    private final FundamentalsDataCompanyRepository fundamentalsDataCompanyRepository;
    private final EconomyRepository economyRepository;
    private final AlphaClient alphaClient;
    private final AlphaClientMarket alphaClientMarket;
    private final AlphaClientFundamentals alphaClientFundamentals;
    private final AlphaClientEconomy alphaClientEconomy;

    @Autowired
    public DataLoadingService(@Qualifier("vectorStoreDB") VectorStore vectorStore,
                              StockNewsAndSentimentalsRepository stockRepository,
                              CryptoNewsAndSentimentalsRepository cryptoRepository,
                              StockClassificationRepository stockClassificationRepository,
                              StockMarketDataRepository stockMarketDataRepository,
                              FundamentalsDataCompanyRepository fundamentalsDataCompanyRepository, EconomyRepository economyRepository,
                              AlphaClient alphaClient,
                              AlphaClientMarket alphaClientMarket,
                              AlphaClientFundamentals alphaClientFundamentals,
                              AlphaClientEconomy alphaClientEconomy) {
        this.vectorStore = vectorStore;
        this.stockRepository = stockRepository;
        this.cryptoRepository = cryptoRepository;
        this.stockClassificationRepository = stockClassificationRepository;
        this.stockMarketDataRepository = stockMarketDataRepository;
        this.fundamentalsDataCompanyRepository = fundamentalsDataCompanyRepository;
        this.economyRepository = economyRepository;
        this.alphaClient = alphaClient;
        this.alphaClientMarket = alphaClientMarket;
        this.alphaClientFundamentals = alphaClientFundamentals;
        this.alphaClientEconomy = alphaClientEconomy;
    }

    public void loadData() {
        log.info("Starting DataLoadingService.");
        economuData();
        handleStockClassification();
        processEntities(cryptoRepository, CryptoEnum.values(), this::processCryptoNewsAndSentimentals);
        processEntities(stockRepository, StockEnum.values(), this::processStockNewsAndSentimentals);
        processEntities(stockMarketDataRepository, StockEnum.values(), this::processStockMarket);
        processEntities(fundamentalsDataCompanyRepository, StockEnum.values(), this::processFundamentalsDataCompany);
        log.info("DataLoadingService completed.");
    }

    private void economuData() {
        EconomyData economyData = alphaClientEconomy.requestEconomy();
        if (ObjectUtils.isEmpty(economyData)) {
            return;
        }
        economyRepository.save(economyData);
        saveVectorDb(economyData.getContentForLLM());
    }

    private void handleStockClassification() {
        StockClassification stockClassification = alphaClient.requestGainersLosers();
        if (ObjectUtils.isEmpty(stockClassification)) {
            return;
        }
        stockClassificationRepository.save(stockClassification);
        saveVectorDb(stockClassification.getContentForLLM());
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

    private void processStockNewsAndSentimentals(StockEnum stockEnum) {
        try {
            StockNewsAndSentimentals stockNewsAndSentimentals = alphaClient.requestStock(stockEnum.getTicker());
            processEntity(stockNewsAndSentimentals, stockRepository::save);
        } catch (Exception e) {
            log.error("Error processing stock: {}", stockEnum.getTicker(), e);
        }
    }

    private void processCryptoNewsAndSentimentals(CryptoEnum cryptoEnum) {
        try {
            CryptoNewsAndSentimentals cryptoNewsAndSentimentals = alphaClient.requestCrypto(cryptoEnum.getTicker());
            processEntity(cryptoNewsAndSentimentals, cryptoRepository::save);
        } catch (Exception e) {
            log.error("Error processing crypto: {}", cryptoEnum.getTicker(), e);
        }
    }

    private void processStockMarket(StockEnum stockEnum) {
        try {
            Stock stock = alphaClientMarket.requestMarketData(stockEnum.getTicker());
            processEntity(stock, stockMarketDataRepository::save);
        } catch (Exception e) {
            log.error("Error processing ticker: {}", stockEnum.getTicker(), e);
        }
    }

    private void processFundamentalsDataCompany(StockEnum stockEnum) {
        try {
            FundamentalsDataCompany fundamentalsDataCompany = alphaClientFundamentals.requestFundamentalsData(stockEnum.getTicker());
            processEntity(fundamentalsDataCompany, fundamentalsDataCompanyRepository::save);
        } catch (Exception e) {
            log.error("Error processing ticker: {}", stockEnum.getTicker(), e);
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
        if (entity instanceof StockNewsAndSentimentals) {
            return ((StockNewsAndSentimentals) entity).getContentforLLM();
        } else if (entity instanceof CryptoNewsAndSentimentals) {
            return ((CryptoNewsAndSentimentals) entity).getContentforLLM();
        } else if (entity instanceof Stock) {
            return ((Stock) entity).getContentForLLM();
        } else if (entity instanceof FundamentalsDataCompany) {
            return ((FundamentalsDataCompany) entity).getContentForLLM();
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