package com.challenge.simplepicpay.impl.authorization;

import com.challenge.simplepicpay.entities.user.User;
import com.challenge.simplepicpay.interfaces.AuthorizationService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyAuthorizationService implements AuthorizationService {

    @Override
    public Boolean authorize(User user, BigDecimal value) {
        return true;
    }
}
