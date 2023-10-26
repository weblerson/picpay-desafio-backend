package com.challenge.simplepicpay.impl.notification;

import com.challenge.simplepicpay.dto.notification.NotificationDTO;
import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.exceptions.NotificationException;
import com.challenge.simplepicpay.interfaces.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ChallengeNotificationService implements NotificationService {

    @Value("${notification-url}")
    private String notificationUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ChallengeNotificationService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    @Override
    public void send(User user, String message) {

        NotificationDTO notificationRequest = new NotificationDTO(user.getEmail(), message);
        ResponseEntity<Map> notificationResponse = this.restTemplate.postForEntity(
                this.notificationUrl, notificationRequest, Map.class
        );

        if (! (notificationResponse.getStatusCode() == HttpStatus.OK))
            throw new NotificationException("Serviço de notificação fora do ar.");
    }
}
