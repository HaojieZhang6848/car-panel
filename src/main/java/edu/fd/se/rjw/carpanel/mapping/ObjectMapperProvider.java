package edu.fd.se.rjw.carpanel.mapping;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ObjectMapperProvider {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
