package com.challenge.simplepicpay.interfaces;

import com.challenge.simplepicpay.entities.user.User;

public interface NotificationService {

    void send(User user, String message);
}
