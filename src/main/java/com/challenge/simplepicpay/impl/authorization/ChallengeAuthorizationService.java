package com.challenge.simplepicpay.impl.authorization;

import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.interfaces.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class ChallengeAuthorizationService implements AuthorizationService {

    @Value("${authorization-url}")
    private String authorizationUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ChallengeAuthorizationService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    @Override
    public Boolean authorize(User user, BigDecimal value) {

        ResponseEntity<Map> authorizationResponse = this.restTemplate.getForEntity(
                this.authorizationUrl, Map.class
        );

        return authorizationResponse.getStatusCode() == HttpStatus.OK;
    }
}
