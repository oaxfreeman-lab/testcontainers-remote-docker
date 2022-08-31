package oax.freeman.testcontainers.infrastructue;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class Messaging<T> {
    private final Sinks.Many<T> sink;
    private final Flux<T> source;
    public void sendMessage(T message) {
        this.sink.tryEmitNext(message).orThrow();
    }
    public void onMessage(Consumer<T> consumer) {
        this.source.subscribe(consumer);
    }
}
