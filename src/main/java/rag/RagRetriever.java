package rag;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

public class RagRetriever {

    @Produces
    @ApplicationScoped
    public RetrievalAugmentor create(final EmbeddingStore store, final EmbeddingModel model) {
        var contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(3)
                .build();

        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .contentInjector((list, chatMessage) -> {
                    StringBuffer prompt = new StringBuffer(((UserMessage)chatMessage).singleText());
                    prompt.append("\nPlease, only use the following information:\n");
                    list.forEach(content -> prompt.append("- ").append(content.textSegment().text()).append("\n"));
                    return new UserMessage(prompt.toString());
                })
                .build();
    }
}
