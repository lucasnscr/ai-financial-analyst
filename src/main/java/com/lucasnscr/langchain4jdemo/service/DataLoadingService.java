package com.lucasnscr.langchain4jdemo.service;

import com.lucasnscr.langchain4jdemo.client.AlphaClient;
import com.lucasnscr.langchain4jdemo.model.Crypto;
import com.lucasnscr.langchain4jdemo.model.CryptoEnum;
import com.lucasnscr.langchain4jdemo.model.StockEnum;
import com.lucasnscr.langchain4jdemo.repository.MongoCryptoRepository;
import com.lucasnscr.langchain4jdemo.repository.MongoStockRepository;
import com.lucasnscr.langchain4jdemo.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    public DataLoadingService(VectorStore vectorStore,
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
                DocumentReader documentReader = createDocumentReader(stock.getContentforLLM());
                processDocumentReader(documentReader);
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
                DocumentReader documentReader = createDocumentReader(crypto.getContentforLLM());
                processDocumentReader(documentReader);
            }
        } catch (Exception e) {
            log.error("Error processing crypto: " + cryptoEnum.getTicker(), e);
            throw new RuntimeException(e);
        }
    }

    private void processDocumentReader(DocumentReader documentReader) {
        try {
            saveEmbedding(documentReader);
        } catch (Exception e) {
            log.error("Error processing document reader.", e);
            throw new RuntimeException(e);
        }
    }

    private DocumentReader createDocumentReader(List<String> content) {
//        String textLlm = formatStock(ticker, jsonObject);
        return  new PagePdfDocumentReader(
                String.valueOf(content),
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                .withNumberOfBottomTextLinesToDelete(3)
                                .withNumberOfTopPagesToSkipBeforeDelete(1)
                                .build())
                        .withPagesPerDocument(1)
                        .build());
    }

    private void saveEmbedding(DocumentReader documentReader) {
        var textSplitter = new TokenTextSplitter();
        log.info("Parsing document, splitting, creating embeddings and storing in vector store...  this will take a while.");
        this.vectorStore.accept(textSplitter.apply(documentReader.get()));
        log.info("Done parsing document, splitting, creating embeddings and storing in vector store");
    }
}