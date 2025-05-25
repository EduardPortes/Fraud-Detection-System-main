package com.eduardoportes.payment_gateway_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eduardoportes.payment_gateway_api.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT f FROM User f WHERE f.name = :name")
    Optional<User> findByName(String name);

    @Query("SELECT f FROM User f WHERE f.cardNumber = :cardNumber")
    Optional<User> findByCardNumber(String cardNumber);
}
