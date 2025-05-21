package com.eduardoportes.payment_gateway_api.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "users")
public class User implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "pass")
    private String pass;

    @Column(name = "cardnumber")
    private Long cardNumber;

    @Column(name = "fraudscore")
    private Integer fraudScore; // Pontuação de risco (0 - 100)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transations = new ArrayList<>();

}
