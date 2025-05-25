package com.eduardoportes.payment_gateway_api.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.eduardoportes.payment_gateway_api.models.enums.TransationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Builder

@Getter
@Setter

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = Shape.STRING)
    private TransationStatus status; // PENDING, PROCESSED, FRAUD

    @Column(name =  "amount", nullable = false)
    private BigDecimal amount;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name =  "timestamp", insertable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name =  "location", nullable = false)
    private String location;


}
