package kafkatests;

import com.fasterxml.jackson.databind.JsonNode;
import kafkatests.dto.UserDto;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageProducerService {

    private static final String TOPIC = "registration";
    
	private final KafkaTemplate<String, JsonNode> kafkaTemplate;

    @Autowired
    public KafkaMessageProducerService(KafkaTemplate<String, JsonNode> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(JsonNode jsonNode) {
        kafkaTemplate.send(TOPIC, jsonNode);
    }
}