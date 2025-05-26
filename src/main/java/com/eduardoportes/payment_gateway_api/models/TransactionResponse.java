package com.eduardoportes.payment_gateway_api.models;

import java.math.BigDecimal;

import com.eduardoportes.payment_gateway_api.models.enums.TransationStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class TransactionResponse {

    private String cardNumber;
    private TransationStatus status;
    private BigDecimal amount;

}
