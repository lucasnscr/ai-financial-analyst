package com.lucasnscr.ai_financial_analyst.service;

import com.lucasnscr.ai_financial_analyst.client.classification.AlphaClientClassification;
import com.lucasnscr.ai_financial_analyst.client.economy.AlphaClientEconomy;
import com.lucasnscr.ai_financial_analyst.client.fundamentals.AlphaClientFundamentals;
import com.lucasnscr.ai_financial_analyst.client.market.AlphaClientMarket;
import com.lucasnscr.ai_financial_analyst.client.newsAndSentimentals.AlphaClientNewsSentimentals;
import com.lucasnscr.ai_financial_analyst.client.technical.AlphaClientTechnical;
import com.lucasnscr.ai_financial_analyst.enums.CryptoEnum;
import com.lucasnscr.ai_financial_analyst.enums.StockEnum;
import com.lucasnscr.ai_financial_analyst.model.economy.EconomyData;
import com.lucasnscr.ai_financial_analyst.model.fundamentals.FundamentalsDataCompany;
import com.lucasnscr.ai_financial_analyst.model.market.Stock;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.CryptoNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.newsAndSentimentals.StockNewsAndSentimentals;
import com.lucasnscr.ai_financial_analyst.model.classification.StockClassification;
import com.lucasnscr.ai_financial_analyst.model.technical.Technical;
import com.lucasnscr.ai_financial_analyst.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@Service
public class DataLoadingService {

    private static final Logger log = LoggerFactory.getLogger(DataLoadingService.class);

    private final AIFinancialRepository aIFinancialRepository;
    private final StockNewsAndSentimentalsRepository stockRepository;
    private final CryptoNewsAndSentimentalsRepository cryptoRepository;
    private final StockClassificationRepository stockClassificationRepository;
    private final StockMarketDataRepository stockMarketDataRepository;
    private final FundamentalsDataCompanyRepository fundamentalsDataCompanyRepository;
    private final TechnicalRepository technicalRepository;
    private final EconomyRepository economyRepository;
    private final AlphaClientMarket alphaClientMarket;
    private final AlphaClientFundamentals alphaClientFundamentals;
    private final AlphaClientEconomy alphaClientEconomy;
    private final AlphaClientClassification alphaClientClassification;
    private final AlphaClientNewsSentimentals alphaClientNewsSentimentals;
    private final AlphaClientTechnical alphaClientTechnical;

    public DataLoadingService(
            AIFinancialRepository aIFinancialRepository, StockNewsAndSentimentalsRepository stockRepository,
            CryptoNewsAndSentimentalsRepository cryptoRepository,
            StockClassificationRepository stockClassificationRepository,
            StockMarketDataRepository stockMarketDataRepository,
            FundamentalsDataCompanyRepository fundamentalsDataCompanyRepository,
            TechnicalRepository technicalRepository,
            EconomyRepository economyRepository,
            AlphaClientMarket alphaClientMarket,
            AlphaClientFundamentals alphaClientFundamentals,
            AlphaClientEconomy alphaClientEconomy,
            AlphaClientClassification alphaClientClassification,
            AlphaClientNewsSentimentals alphaClientNewsSentimentals,
            AlphaClientTechnical alphaClientTechnical) {
        this.aIFinancialRepository = aIFinancialRepository;
        this.stockRepository = stockRepository;
        this.cryptoRepository = cryptoRepository;
        this.stockClassificationRepository = stockClassificationRepository;
        this.stockMarketDataRepository = stockMarketDataRepository;
        this.fundamentalsDataCompanyRepository = fundamentalsDataCompanyRepository;
        this.technicalRepository = technicalRepository;
        this.economyRepository = economyRepository;
        this.alphaClientClassification = alphaClientClassification;
        this.alphaClientNewsSentimentals = alphaClientNewsSentimentals;
        this.alphaClientTechnical = alphaClientTechnical;
        this.alphaClientMarket = alphaClientMarket;
        this.alphaClientFundamentals = alphaClientFundamentals;
        this.alphaClientEconomy = alphaClientEconomy;
    }

    public void loadData() {
        log.info("Starting DataLoadingService.");
        economyData();
        handleStockClassification();
        processEntities(cryptoRepository, CryptoEnum.values(), this::processCryptoNewsAndSentimentals);
        processEntities(stockRepository, StockEnum.values(), this::processStockNewsAndSentimentals);
        processEntities(stockMarketDataRepository, StockEnum.values(), this::processStockMarket);
        processEntities(fundamentalsDataCompanyRepository, StockEnum.values(), this::processFundamentalsDataCompany);
        processEntities(technicalRepository, StockEnum.values(), this::processTechnical);
        log.info("DataLoadingService completed.");
    }

    private void economyData() {
        EconomyData economyData = alphaClientEconomy.requestEconomy();
        if (ObjectUtils.isEmpty(economyData)) {
            return;
        }
        economyRepository.save(economyData);
        aIFinancialRepository.saveVectorDb(economyData.getContentForLLM());
    }

    private void handleStockClassification() {
        StockClassification stockClassification = alphaClientClassification.requestGainersLosers();
        if (ObjectUtils.isEmpty(stockClassification)) {
            return;
        }
        stockClassificationRepository.save(stockClassification);
        aIFinancialRepository.saveVectorDb(stockClassification.getContentForLLM());
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
            StockNewsAndSentimentals stockNewsAndSentimentals = alphaClientNewsSentimentals.requestStock(stockEnum.getTicker());
            processEntity(stockNewsAndSentimentals, stockRepository::save);
        } catch (Exception e) {
            log.error("Error processing stock: {}", stockEnum.getTicker(), e);
        }
    }

    private void processCryptoNewsAndSentimentals(CryptoEnum cryptoEnum) {
        try {
            CryptoNewsAndSentimentals cryptoNewsAndSentimentals = alphaClientNewsSentimentals.requestCrypto(cryptoEnum.getTicker());
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

    private void processTechnical(StockEnum stockEnum) {
        try {
            Technical technical = alphaClientTechnical.requestTechnical(stockEnum.getTicker());
            processEntity(technical, technicalRepository::save);
        } catch (Exception e) {
            log.error("Error processing ticker: {}", stockEnum.getTicker(), e);
        }
    }

    private <T> void processEntity(T entity, Consumer<T> saveFunction) {
        if (ObjectUtils.isEmpty(entity)) {
            return;
        }
        saveFunction.accept(entity);
        aIFinancialRepository.saveVectorDb(getContentForLLM(entity));
    }

    private List<String> getContentForLLM(Object entity) {
        if (entity instanceof StockNewsAndSentimentals) {
            return ((StockNewsAndSentimentals) entity).getContentforLLM();
        } else if (entity instanceof CryptoNewsAndSentimentals) {
            return ((CryptoNewsAndSentimentals) entity).getContentforLLM();
        } else if (entity instanceof Stock) {
            return ((Stock) entity).getContentForLLM();
        } else if (entity instanceof StockClassification) {
            return ((StockClassification) entity).getContentForLLM();
        } else if (entity instanceof FundamentalsDataCompany) {
            return ((FundamentalsDataCompany) entity).getContentForLLM();
        } else if (entity instanceof EconomyData) {
            return ((EconomyData) entity).getContentForLLM();
        } else if (entity instanceof Technical) {
            return ((Technical) entity).getContentForLLM();
        }
        return Collections.emptyList();
    }

    public void saveVectorDb(List<String> contentList) {
        aIFinancialRepository.saveVectorDb(contentList);
    }
}