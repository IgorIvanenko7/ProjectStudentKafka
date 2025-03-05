package kafkatests;

import kafkatests.config.ConfigPropertiesAES;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Log4j2
@UtilityClass
public class KafkaConfigFactory {

    public static <K, T> void sendMessage(T message, KafkaTemplate<K, T> kafkaTemplate, String topic)
            throws InterruptedException, ExecutionException, TimeoutException {
        var future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<>() {

            @Override
            public void onSuccess(SendResult<K, T> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to: {}", message, ex.getMessage());
            }
        });
        /* необходим для дебага, так как переводит основной поток (в котором выполняем DEBUG) в состояние -> TIMED_WAITING =>
        позволяя другому потоку завершить работу => выполняется send в топик Kafka (or Thread.sleep(500)) */
        future.get(3, TimeUnit.SECONDS);
    }

    public static <T> ProducerFactory<String, T> createKafkaProducerFactory(ConfigPropertiesAES ccmKafkaProperties) {
        var configProps = new HashMap<String, Object>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                ccmKafkaProperties.getSslKafka().get(ccmKafkaProperties.getSegment()).getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configSslConnection(configProps, ccmKafkaProperties);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public static <T> ConsumerFactory<String, T> createKafkaConsumerFactory(ConfigPropertiesAES ccmKafkaProperties) {
        var configProps = new HashMap<String, Object>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                ccmKafkaProperties.getSslKafka().get(ccmKafkaProperties.getSegment()).getBootstrapServers());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, ccmKafkaProperties.getConsumer().getGroupId());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, ccmKafkaProperties.getConsumer().getAutoOffsetReset());
        configSslConnection(configProps, ccmKafkaProperties);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    public static void configSslConnection(Map<String, Object> factoryConfig, ConfigPropertiesAES configPropertiesAES) {

            var sslKafka = configPropertiesAES.getSslKafka().get(configPropertiesAES.getSegment());
            var locationTrustStore =  KafkaConfigFactory.class.getClassLoader().getResource(sslKafka.getTrustStoreLocation()).getPath();
            var locationKeyStore =  KafkaConfigFactory.class.getClassLoader().getResource(sslKafka.getKeyStoreLocation()).getPath();

            locationTrustStore = Optional.ofNullable(locationTrustStore)
                    .filter(pathStr -> pathStr.startsWith("/") && !pathStr.contains("agent"))
                        .map(pathStr -> pathStr.substring(1))
                                            .orElse(locationTrustStore);
            locationKeyStore = Optional.ofNullable(locationKeyStore)
                    .filter(pathStr -> pathStr.startsWith("/") && !pathStr.contains("agent"))
                    .map(pathStr -> pathStr.substring(1))
                    .orElse(locationKeyStore);

            factoryConfig.put("security.protocol", "SSL");
            factoryConfig.put("ssl.truststore.location", locationTrustStore);
            factoryConfig.put("ssl.truststore.password", sslKafka.getTrustStorePassword());
            factoryConfig.put("ssl.truststore.type", sslKafka.getTrustStoreType());
            factoryConfig.put("ssl.key.password", sslKafka.getKeyPassword());
            factoryConfig.put("ssl.keystore.password", sslKafka.getKeyStorePassword());
            factoryConfig.put("ssl.keystore.location", locationKeyStore);
            factoryConfig.put("ssl.keystore.type", sslKafka.getKeyStoreType());
            factoryConfig.put("enable.auto.commit", sslKafka.getAutoCommit());
    }

    public static <T> ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainerFactory(
            ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
