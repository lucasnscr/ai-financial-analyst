package com.lucasnscr.ai_financial_analyst.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VectorStoreRepository {

    private final VectorStore vectorStore;
    private final OpenAiEmbeddingModel embeddingModel;

    public VectorStoreRepository(VectorStore vectorStore,
                                 OpenAiEmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    public void saveVectorDb(List<String> contentList) {
        if (CollectionUtils.isEmpty(contentList)) {
            return;
        }

        var textSplitter = new TokenTextSplitter();
        List<Document> documentList = textSplitter.apply(contentList.stream().map(Document::new).collect(Collectors.toList()));
        log.info("Parsing document, splitting, creating embeddings and storing in vector store...  this will take a while.");
        for (Document document : documentList){
            if (document != null) {
                document.setEmbedding(embeddingModel.embed(document));
                vectorStore.add(List.of(document));
            }
        }
        log.info("Done parsing document, splitting, creating embeddings and storing in vector store");
    }
}
