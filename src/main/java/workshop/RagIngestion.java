package workshop;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenCountEstimator;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.file.Path;
import java.util.List;

import static dev.langchain4j.data.document.splitter.DocumentSplitters.recursive;

@ApplicationScoped
public class RagIngestion {

    /**
     * Ingests the documents from the given location into the embedding store.
     *
     * @param ev             the startup event to trigger the ingestion when the application starts
     * @param embeddingStore the embedding store the embedding store (PostGreSQL in our case)
     * @param embeddingModel the embedding model to use for the embedding (BGE-Small-EN-Quantized in our case)
     * @param documentsPath  the location of the documents to ingest
     */
    public void ingest(@Observes final StartupEvent ev,
                       final EmbeddingStore embeddingStore,
                       final EmbeddingModel embeddingModel,
                       @ConfigProperty(name = "rag.location") final Path documentsPath) {
        embeddingStore.removeAll(); // cleanup the store to start fresh (just for demo purposes)

        final EmbeddingStoreIngestor ingestor = createIngestor(embeddingStore, embeddingModel);
        final List<Document> documents = FileSystemDocumentLoader.loadDocumentsRecursively(documentsPath);
        ingestor.ingest(documents);

        Log.info("Documents ingested successfully");
    }

    private EmbeddingStoreIngestor createIngestor(final EmbeddingStore embeddingStore, final EmbeddingModel embeddingModel) {
        return EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(recursive(100, 25, new HuggingFaceTokenCountEstimator()))
                .build();
    }
}
