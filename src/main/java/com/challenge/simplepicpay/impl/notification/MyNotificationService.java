package com.challenge.simplepicpay.impl.notification;

import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.interfaces.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class MyNotificationService implements NotificationService {

    @Override
    public void send(User user, String message) {

        System.out.printf("%s: %s", user.getFirstName(), message);
    }
}
