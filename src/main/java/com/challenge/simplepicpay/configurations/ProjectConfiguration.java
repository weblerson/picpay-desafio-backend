package com.challenge.simplepicpay.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProjectConfiguration {

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }
}
