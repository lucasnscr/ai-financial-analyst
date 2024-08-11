package com.lucasnscr.langchain4jdemo.service;

import com.lucasnscr.langchain4jdemo.model.AiAssistantModelCommunication;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ChatService {

    private final AiAssistantModelCommunication assistant;
    private EmbeddingStore embeddingStore;

    @Autowired
    public ChatService(AiAssistantModelCommunication assistant,
                       EmbeddingStore embeddingStore) {
        this.assistant = assistant;
        this.embeddingStore = embeddingStore;
    }

    public CompletableFuture<String> ask(String userPrompt) {
        TokenStream tokenStream = this.assistant.chatWithModel(userPrompt);
        CompletableFuture<String> future = new CompletableFuture<>();
        tokenStream.onNext(System.out::print)
                .onComplete(_ -> {
                    System.out.println();
                    future.complete(null);
                })
                .onError(Throwable::printStackTrace)
                .start();
        return future;
    }

    private static void modelResponse(StreamingChatLanguageModel model, String userPrompt) {
        CompletableFuture<Response<AiMessage>> futureResponse = new CompletableFuture<>();
        model.generate(userPrompt, new StreamingResponseHandler<>() {
            @Override
            public void onNext(String token) {
                System.out.print(token);
            }
            @Override
            public void onComplete(Response<AiMessage> response) {
                futureResponse.complete(response);
            }
            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });
        futureResponse.join();
    }

    private String retrieveData(String ticker){
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        Embedding queryEmbedding = embeddingModel.embed("What is condition for my stock " + ticker + " today").content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(queryEmbedding, 1);
        EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);
        return embeddingMatch.embedded().text();
    }


}
