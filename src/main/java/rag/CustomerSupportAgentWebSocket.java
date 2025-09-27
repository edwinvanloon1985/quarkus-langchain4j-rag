package rag;

import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.mutiny.Multi;
import jakarta.json.Json;

import java.time.Duration;
import java.util.UUID;

@WebSocket(path = "/customer-support-agent")
public record CustomerSupportAgentWebSocket(CustomerSupportAgent customerSupportAgent) {

    @OnTextMessage
    public Multi<String> onTextMessage(final String message) {
        // Unique id per request.
        final var messageId = UUID.randomUUID().toString();

        // Receiving tokens (over time) from LLM.
        final var tokens = customerSupportAgent.chat(message)
                .onFailure(this::isStatus429)
                .retry()
                .withBackOff(Duration.ofSeconds(1), Duration.ofSeconds(5))
                .atMost(3);

        // Construct delta (a stream of tokens received from the LLM asynchronously).
        final var deltas = tokens.onItem().transform(token -> Json.createObjectBuilder()
                .add("type", "delta")
                .add("id", messageId)
                .add("content", token)
                .build()
                .toString());
        // Construct done (a simple mutiny object with a done flag)
        final var done = Multi.createFrom().item(Json.createObjectBuilder()
                .add("type", "done")
                .add("id", messageId)
                .build()
                .toString());

        // This is where the magic happens. Mutiny concatenates the streams in order.
        // So it handles the deltas first until all tokens are processed, then it will send the done.
        return Multi.createBy().concatenating().streams(deltas, done);
    }

    private boolean isStatus429(final Throwable throwable) {
        return throwable instanceof jakarta.ws.rs.WebApplicationException
                && ((jakarta.ws.rs.WebApplicationException) throwable).getResponse().getStatus() == 429;
    }
}
