package oax.freeman.testcontainers.infrastructue;

import lombok.RequiredArgsConstructor;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class Messaging<T> {
    private final Sinks.Many<T> sink;
    private final Flux<T> source;

    public void emit(T message) {
        this.sink.tryEmitNext(message).orThrow();
    }

    public Disposable subscribe(Consumer<T> consumer) {
        return this.source.subscribe(consumer);
    }

    public Flux<T> messageSource() {
        return this.source;
    }
}
