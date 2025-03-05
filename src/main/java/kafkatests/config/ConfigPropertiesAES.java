package kafkatests.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Optional;

@Log4j2
@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring")
public class ConfigPropertiesAES {

    private String segment;
    private Pass pass ;
    private Map<String, String> datasource;
    private Map<String, String> urlToken;
    private Map<String, String> url;
    private Map<String, ConfKafka> sslKafka;
    private Consumer consumer;

    @Data
    public static class Pass {
        private String key;
        private String segment;
    }

    @Data
    public static class Consumer {
        private String groupId;
        private String autoOffsetReset;
    }

    @Data
    public static class ConfKafka {
        private String bootstrapServers;
        private String trustStoreLocation;
        private String trustStorePassword;
        private String keyStoreLocation;
        private String keyStorePassword;
        private String keyPassword;
        private String trustStoreType;
        private String keyStoreType;
        private String autoCommit;
    }

    public String getValueProp(String keyfind, Map<String, String> sourceProp) {
       return Optional.ofNullable(sourceProp)
               .map(map -> map.get(keyfind))
               .orElseThrow(() -> new RuntimeException("# Not found properties #"));
    }
}