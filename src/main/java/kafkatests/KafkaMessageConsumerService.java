package kafkatests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafkatests.dto.UserDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service

public class KafkaMessageConsumerService {

//    @Autowired
//	public ObjectMapper objectMapper;

	public static Map<String, List<JsonNode>> storeMess = new HashMap<>();

	@KafkaListener(topics = "registration", groupId = "myGroup")
	public void listener(JsonNode message) throws JsonProcessingException {

//		var userDto = objectMapper.readValue(message, UserDto.class);
//		var findList = storeMess.getOrDefault(userDto.getConferenceId(), new ArrayList<>());
//		findList.add(userDto.getName());
		log.info("### Received message: {} ###", message);
	}
}