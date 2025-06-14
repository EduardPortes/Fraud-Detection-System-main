package com.eduardoportes.payment_gateway_api.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter

@Entity
@Table(name = "fraud_alerts")
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transationid", nullable = false)
    private Transaction transation;

    @ManyToOne
    @JoinColumn(name = "rule_violated", nullable = false)
    private FraudRule ruleViolated;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
