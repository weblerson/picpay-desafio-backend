package com.challenge.simplepicpay.services;

import com.challenge.simplepicpay.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Value("${notification-url}")
    private String notificationUrl;

    private final RestTemplate restTemplate;
    private final com.challenge.simplepicpay.interfaces.NotificationService challengeNotificationService;
    private final com.challenge.simplepicpay.interfaces.NotificationService myNotificationService;

    @Autowired
    public NotificationService(
            RestTemplate restTemplate,
            com.challenge.simplepicpay.interfaces.NotificationService challengeNotificationService,
            com.challenge.simplepicpay.interfaces.NotificationService myNotificationService
    ) {

        this.restTemplate = restTemplate;
        this.challengeNotificationService = challengeNotificationService;
        this.myNotificationService = myNotificationService;
    }

    public void notificate(User user, String message) {

        // Mock de notificação do desafio indisponível
        // this.challengeNotificationService.notificate(user, message);

        this.myNotificationService.send(user, message);
    }
}
