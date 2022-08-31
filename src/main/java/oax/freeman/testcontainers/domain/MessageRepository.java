package oax.freeman.testcontainers.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAll();

    Optional<MessageEntity> findById(Long id);

    List<MessageEntity> findByType(MessageType type);

    MessageEntity save(MessageEntity messageEntity);

}
