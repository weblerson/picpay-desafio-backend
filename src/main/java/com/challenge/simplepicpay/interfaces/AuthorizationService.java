package com.challenge.simplepicpay.interfaces;

import com.challenge.simplepicpay.entities.user.User;

import java.math.BigDecimal;

public interface AuthorizationService {

    Boolean authorize(User user, BigDecimal value);
}
