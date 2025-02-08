package com.lucasnscr.ai_financial_analyst.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
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
    private final OpenAiEmbeddingModel embeddingModel;
    private final RetrievalAugmentationAdvisor retrievalAugmentationAdvisor;
    private final ChatModel chatModel;

    public AIFinancialRepository(VectorStore vectorStore, OpenAiEmbeddingModel embeddingModel, ChatModel chatModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.chatModel = chatModel;
        this.retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(createQueryTransformer())
                .documentRetriever(createDocumentRetriever())
                .queryAugmenter(createQueryAugmenter())
                .build();
    }

    /**
     * Salva documentos no banco vetorial.
     */
    public void saveVectorDb(List<String> contentList) {
        if (CollectionUtils.isEmpty(contentList)) {
            return;
        }

        TokenTextSplitter textSplitter = new TokenTextSplitter();
        List<Document> documentList = textSplitter.apply(
                contentList.stream().map(Document::new).collect(Collectors.toList())
        );

        log.info("Parsing documents, splitting, creating embeddings, and storing in vector store... This will take a while.");

        documentList.parallelStream()
                .filter(Objects::nonNull)
                .forEach(document -> {
                    Object embedding = embeddingModel.embed(document);
                    document.getMetadata().put("embedding", embedding);
                });

        vectorStore.add(documentList);
        log.info("Done parsing documents, creating embeddings, and storing in vector store.");
    }

    public List<Document> retrieveRelevantDocuments(String query) {
        AdvisedRequest request = AdvisedRequest.builder()
                .userText(query)
//                .userParams(Map.of())
                .build();

        AdvisedRequest advisedRequest = retrievalAugmentationAdvisor.before(request);
        Object contextValue = advisedRequest.adviseContext().get(RetrievalAugmentationAdvisor.DOCUMENT_CONTEXT);
        if (contextValue instanceof List<?> documents) {
            return (List<Document>) documents;
        }
        return List.of();
    }

    /**
     * Configura o Query Transformer (Pré-Retrieval).
     * Reformula perguntas mal estruturadas antes da busca.
     */
    private QueryTransformer createQueryTransformer() {
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
                                new Filter.Value("financial_reports")
                        )
                )
                .build();
    }

    /**
     * Configura o Query Augmenter (Pós-Retrieval).
     * Adiciona contexto à consulta antes da geração da resposta.
     */
    private QueryAugmenter createQueryAugmenter() {
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                .build();
    }

}
