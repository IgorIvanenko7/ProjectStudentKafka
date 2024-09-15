package kafkatests.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafkatests.KafkaMessageConsumerService;
import kafkatests.KafkaMessageProducerService;
import kafkatests.dto.UserDto;
import kafkatests.handleExeption.HandlerExeptionLimit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;


@Log4j2
@Service
@RequiredArgsConstructor
public class HandlerService {

    private final KafkaMessageProducerService kafkaMessageProducerService;
    private final ObjectMapper objectMapper;
    private final ConvertData convertData;

    public static Map<String, List<String>> storeUser = new HashMap<>();

    public void regUsers (UserDto userDto) {
        // Insert into
       var reqDto = Optional.ofNullable(userDto)
               .map(dto -> {
                  // Если нет указанной конференции создается новая
                  var findList = storeUser.getOrDefault(dto.getConferenceId(), new ArrayList<>());
                  findList.add(dto.getName());
                  storeUser.put(dto.getConferenceId(), findList);
                  return storeUser;
               })
               .orElseThrow(() -> new HandlerExeptionLimit(
                        "Пустой объект"," отказ в регистрации"));
        // Insert into Topic
       var jsonObj = objectMapper.valueToTree(userDto);
       log.info(jsonObj);
       kafkaMessageProducerService.send(jsonObj.toString());
       log.info("Insert User: {} on Conference: {}",
               userDto.getName(), userDto.getConferenceId());
    }

    public List<String> getListUsers (String conferenceId) {
        return Optional.ofNullable(storeUser.get(conferenceId))
                .orElseThrow(() -> new HandlerExeptionLimit(
                        "Конференция: " + conferenceId ,"не найдена"));
    }

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
    public List<UserDto> getListUsersV2 (String conferenceId) {

        var listUser = Optional.ofNullable(KafkaMessageConsumerService.storeMess.get(conferenceId))
                .orElse(Collections.emptyList());
        KafkaMessageConsumerService.storeMess.put(conferenceId, new ArrayList<>());
        return listUser;
    }
}
