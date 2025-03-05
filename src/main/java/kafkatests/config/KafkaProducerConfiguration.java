package kafkatests.config;

import kafkatests.KafkaConfigFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Log4j2
@Setter
@Getter
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class KafkaProducerConfiguration {

    private final ConfigPropertiesAES configPropertiesAES;
    public static final String LIMIT_PROCESS_CREATED_TOPIC
            = "ru.vtb.dc.pcm.integration.external.consume.state.CreditMemorandumInitialized";


    @Bean
    @Qualifier("kafkaConsumerFactory")
    public ConsumerFactory<String, String> clientMappingKafkaConsumerFactory() {
        return KafkaConfigFactory.createKafkaConsumerFactory(configPropertiesAES);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> clientMappingKafkaListenerContainerFactory(
            @Qualifier("kafkaConsumerFactory") ConsumerFactory<String, String> consumerFactory) {
        return KafkaConfigFactory.createKafkaListenerContainerFactory(consumerFactory);
    }

}
