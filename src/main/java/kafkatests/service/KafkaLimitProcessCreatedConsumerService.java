package kafkatests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static kafkatests.config.KafkaProducerConfiguration.LIMIT_PROCESS_CREATED_TOPIC;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaLimitProcessCreatedConsumerService {

	@KafkaListener(
		topics = LIMIT_PROCESS_CREATED_TOPIC,
		containerFactory = "clientMappingKafkaListenerContainerFactory")
	public void listenBase(ConsumerRecord<String, String> record) {
		log.info("### Received record: {} ###", record);
	}
}
