package com.eduardoportes.payment_gateway_api.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TransactionRequest {
    private String cardNumber;
    private BigDecimal amount;
}