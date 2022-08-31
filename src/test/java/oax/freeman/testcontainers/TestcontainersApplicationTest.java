package oax.freeman.testcontainers;

import lombok.SneakyThrows;
import oax.freeman.testcontainers.domain.MessageEntity;
import oax.freeman.testcontainers.domain.MessageRepository;
import oax.freeman.testcontainers.infrastructue.Messaging;
import oax.freeman.testcontainers.infrastructue.SimpleMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static oax.freeman.testcontainers.domain.MessageType.INCOMING;
import static oax.freeman.testcontainers.domain.MessageType.OUTGOING;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class TestcontainersApplicationTest extends TestInfrastructure {

    @Autowired
    Messaging<Message<SimpleMessage>> messaging;

    @Autowired
    MessageRepository messageRepository;

    @Test
    void should_create_test_infrastructure() {
        assertThat(rabbitmqContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @SneakyThrows
    @Test
    void should_send_and_receive_message_from_rabbitmq() {
        assertThat(rabbitmqContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();

        // Given
        String message = "First Message!";
        Message<SimpleMessage> sentMessage = MessageBuilder.withPayload(new SimpleMessage(message)).build();
        AtomicReference<Message<SimpleMessage>> receivedMessage = new AtomicReference<>();

        // When
        messaging.onMessage(receivedMessage::set);
        messaging.sendMessage(sentMessage);
        Thread.sleep(2000); // Messaging delay

        // Then
        assertThat(receivedMessage.get().getPayload().getMessage()).isEqualTo(sentMessage.getPayload().getMessage());
    }

    @Test
    void should_save_and_retrieve_data_from_postgres() {
        assertThat(rabbitmqContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();

        // Given
        String message = "First Entity!";
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message);
        messageEntity.setType(OUTGOING);

        // When
        Long savedMessageId = messageRepository.save(messageEntity).getId();

        // Then
        assertThat(messageRepository.findById(savedMessageId).isPresent()).isTrue();
        assertThat(messageRepository.findById(savedMessageId).get().getMessage()).isEqualTo(message);
    }

    @SneakyThrows
    @Test
    void should_simulate_application_logic() {
        assertThat(rabbitmqContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();

        // Given
        List<Message<SimpleMessage>> sentMessages = Arrays
                .stream(new String[]{
                        "Message 1", "Message 2", "Message 3", "Message 4"
                })
                .map(msg -> MessageBuilder.withPayload(new SimpleMessage(msg)).build())
                .collect(Collectors.toList());

        messaging.onMessage(message -> {
            MessageEntity incomingMessage = new MessageEntity();
            incomingMessage.setMessage(message.getPayload().getMessage());
            incomingMessage.setType(INCOMING);
            messageRepository.save(incomingMessage);
        });

        // When
        sentMessages.forEach(messaging::sendMessage);
        Thread.sleep(2000); // Messaging delay
        List<MessageEntity> messagesSavedFromRabbit = messageRepository.findByType(INCOMING);

        // Then
        assertThat(messagesSavedFromRabbit.size()).isEqualTo(sentMessages.size());
        assertThat(messagesSavedFromRabbit.get(0).getMessage()).isEqualTo(sentMessages.get(0).getPayload().getMessage());
        assertThat(messagesSavedFromRabbit.get(1).getMessage()).isEqualTo(sentMessages.get(1).getPayload().getMessage());
        assertThat(messagesSavedFromRabbit.get(2).getMessage()).isEqualTo(sentMessages.get(2).getPayload().getMessage());
        assertThat(messagesSavedFromRabbit.get(3).getMessage()).isEqualTo(sentMessages.get(3).getPayload().getMessage());
    }
}
