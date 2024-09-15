package kafkatests.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Log4j2
@Setter
@Getter
@Service
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class ConvertData {

    private final ObjectMapper objectMapper;

    public LinkedHashMap<String, Object> convertStringToObjectMap(String path){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return objectMapper.readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //-------------------------------------------------------------------------------------------------
    public  <T>  T getObjectFromFile(Class<T> respClass, String path){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return  objectMapper.readValue(inputStream, respClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  <T>  T getObjectOLD(Class<T> respClass, String content) {
        try {
            return  objectMapper.readValue(content, respClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public  <T>  T getObject(Class<T> respClass, Object content) {
        try {
            return  objectMapper.readValue((String) content, respClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> convertToObjectMap(String path){
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
            return objectMapper.readValue(inputStream, new TypeReference<>(){});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
