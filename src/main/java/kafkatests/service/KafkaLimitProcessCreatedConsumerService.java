package kafkatests.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

import static kafkatests.config.KafkaProducerConfigurationLimit.LIMIT_PROCESS_CREATED_TOPIC;

@Log4j2
@Service
@RequiredArgsConstructor
public class KafkaLimitProcessCreatedConsumerService {

	private final ObjectMapper objectMapper;

	@KafkaListener(
		topics = LIMIT_PROCESS_CREATED_TOPIC,
		containerFactory = "clientMappingKafkaListenerContainerFactory")
	public void listenBase(ConsumerRecord<String, String> record) throws JsonProcessingException {
		var vj = objectMapper.readValue(record.value(), JsonNode.class);
		log.info("### Received jSON value: {} ###", vj);
		var pl = vj.get("payload");
		Map<String, Object> resultMap = objectMapper.readValue(pl.toString(), new TypeReference<>(){});
		log.info("### Payload map value: {} ###", resultMap);
	}
}
