package com.eduardoportes.payment_gateway_api.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.eduardoportes.payment_gateway_api.kafka.KafkaProducerService;
import com.eduardoportes.payment_gateway_api.models.Transaction;
import com.eduardoportes.payment_gateway_api.models.TransactionRequest;
import com.eduardoportes.payment_gateway_api.models.TransactionResponse;
import com.eduardoportes.payment_gateway_api.models.User;
import com.eduardoportes.payment_gateway_api.models.enums.TransationStatus;
import com.eduardoportes.payment_gateway_api.repository.TransactionRepository;
import com.eduardoportes.payment_gateway_api.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private TransactionRepository respository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Value("${spring.kafka.topic}")
    private String topic;

    public TransactionResponse create(TransactionRequest request) {

        userRepository.findByCardNumber(request.getCardNumber())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // Validate request
        if (request == null || request.getCardNumber() == null || request.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Invalid transaction request");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getCredentials();
        String location = jwt.getClaim("location");

        Optional<User> userOpt = userRepository.findByCardNumber(request.getCardNumber());
        if(!userOpt.isEmpty()) {
            Transaction transaction =  Transaction.builder()
                .user(userOpt.get())
                .status(TransationStatus.PENDING)
                .amount(request.getAmount())
                .location(location)
                .build();

            transaction = respository.save(transaction);

            sendToKafka(transaction);

            String card = transaction.getUser().getCardNumber();
            String lastDigits = card.substring(card.length() -3);

            TransactionResponse response = TransactionResponse.builder()
                .cardNumber("****" + lastDigits)
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .build();
            return response;
        } 
        return null;
    }

    private void sendToKafka(Transaction transaction){

        ObjectMapper mapper = new ObjectMapper();
        try {
            String value = mapper.writeValueAsString(transaction);
            kafkaProducerService.sendMessage(topic, value);
        } catch (JsonProcessingException e) {
            logger.error("Error processing transaction for Kafka: ", e);
        }
    }

}
