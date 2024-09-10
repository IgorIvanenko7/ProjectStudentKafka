package kafkatests.controller;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.util.*;


@Controller
public class NewRegistersReceiver {

    @Autowired
    NewRegistersService service;

    public List<String> getNewRegisters(int id) {
        return service.register(id);
    }
}

@Log4j2
@Service
class NewRegistersService {

    Map<Integer, ConferenceConsumer> consumers = new HashMap<>();

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

    public List<String> register (int id) {
        if (!consumers.containsKey(id)) {
            consumers.put(id, new ConferenceConsumer(consumerFactory, id));
        }
        return consumers.get(id).getNewRegisters();
    }
}

@Log4j2
class ConferenceConsumer {

    int id;
    Consumer<String, String> consumer;

    public ConferenceConsumer (ConsumerFactory<String, String> consumerFactory, int id) {
        consumer = consumerFactory.createConsumer("conference" + id, "reader");
        consumer.subscribe(Arrays.asList("registration"));
    }

    public List<String> getNewRegisters() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
        List<String> res = new ArrayList<>();
        for (ConsumerRecord<String, String> record: records) {
            String str = record.value();
            log.info(str + str.contains("\"conferenceId\":" + id));
            if (str.contains("\"conferenceId\":" + id)) {
                res.add(record.value());
            }
        }
        return res;
    }
}
