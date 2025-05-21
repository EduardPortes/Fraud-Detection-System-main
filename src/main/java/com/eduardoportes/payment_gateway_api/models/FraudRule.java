package com.eduardoportes.payment_gateway_api.models;

import com.eduardoportes.payment_gateway_api.models.enums.FraudAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fraud_rules")
public class FraudRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "conditions", nullable = false)
    private String condition;

    @Enumerated(EnumType.STRING)
    private FraudAction action;



}
