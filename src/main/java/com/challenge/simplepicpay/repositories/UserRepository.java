package com.challenge.simplepicpay.repositories;

import com.challenge.simplepicpay.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
