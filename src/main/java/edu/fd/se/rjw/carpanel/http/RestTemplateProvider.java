package edu.fd.se.rjw.carpanel.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateProvider {
    
    @Bean
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
