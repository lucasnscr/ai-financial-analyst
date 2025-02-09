package com.lucasnscr.ai_financial_analyst.repository;

import com.lucasnscr.ai_financial_analyst.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AIFinancialRepository {

    private static final Logger log = LoggerFactory.getLogger(AIFinancialRepository.class);

    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final RetrievalAugmentationAdvisor retrievalAugmentationAdvisor;
    private final ChatModel chatModel;

    public AIFinancialRepository(VectorStore vectorStore, EmbeddingModel embeddingModel, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.chatModel = chatModel;
        this.retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(createQueryTransformers())
                .documentRetriever(createDocumentRetriever())
                .queryAugmenter(createQueryAugmenter())
                .build();
    }

    public void saveVectorDb(List<String> contentList, Metadata metadata) {
        if (CollectionUtils.isEmpty(contentList)) {
            return;
        }

        TokenTextSplitter textSplitter = new TokenTextSplitter();
        List<Document> documentList = textSplitter.apply(
                contentList.stream().map(Document::new).collect(Collectors.toList()));

        log.info("Parsing documents, splitting, creating embeddings, and storing in vector store... This will take a while.");
        documentList.parallelStream()
                .filter(Objects::nonNull)
                .forEach(document -> {
                    document.getMetadata().put("metadata", metadata);
                    embeddingModel.embed(document);
                });
        vectorStore.add(documentList);
        log.info("Done parsing documents, creating embeddings, and storing in vector store.");
    }

    public List<Document> retrieveRelevantDocuments(String query) {
        AdvisedRequest request = AdvisedRequest.builder()
                .chatModel(chatModel)
                .userText(query)
                .build();

        AdvisedRequest advisedRequest = retrievalAugmentationAdvisor.before(request);
        Object contextValue = advisedRequest.adviseContext().get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT);
        if (contextValue instanceof List<?> documents) {
            return (List<Document>) documents;
        }
        return List.of();
    }

    private QueryTransformer createQueryTransformers() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        return RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClient.mutate())
                .build();
    }

    private VectorStoreDocumentRetriever createDocumentRetriever() {
        return VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.80)
                .topK(5)
                .filterExpression(
                        new Filter.Expression(
                                Filter.ExpressionType.EQ,
                                new Filter.Key("source"),
                                new Filter.Value("financial_reports")))
                .build();
    }

    private QueryAugmenter createQueryAugmenter() {
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .build();
    }

}
