package oax.freeman.testcontainers.infrastructue;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class AMPQBindings {

    private final Sinks.Many<Message<SimpleMessage>> incomingMessages =
            Sinks.many().multicast().onBackpressureBuffer();

    private final Sinks.Many<Message<SimpleMessage>> outgoingMessages =
            Sinks.many().multicast().onBackpressureBuffer();

    @Bean
    Consumer<Message<SimpleMessage>> messageReceiveBinding() {
        return incomingMessages::tryEmitNext;
    }

    @Bean
    Supplier<Flux<Message<SimpleMessage>>> messageBroadcastBinding() {
        return outgoingMessages::asFlux;
    }

    @Bean
    Messaging<Message<SimpleMessage>> messaging() {
        return new Messaging<>(this.outgoingMessages, this.incomingMessages.asFlux());
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

