package com.eduardoportes.payment_gateway_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eduardoportes.payment_gateway_api.models.Transaction;

@Repository
public interface  TransactionRepository extends JpaRepository<Transaction, Long> {
    
}
