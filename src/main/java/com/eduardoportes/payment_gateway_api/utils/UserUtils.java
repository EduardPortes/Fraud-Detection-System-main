package com.eduardoportes.payment_gateway_api.utils;

import com.eduardoportes.payment_gateway_api.models.User;
import com.eduardoportes.payment_gateway_api.models.dto.UserDTO;

public class UserUtils {
    
    public static User dtoToUser(UserDTO userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setPass(userDto.getPass());
        user.setCardNumber(Long.parseLong(generateCardNumber()));
        user.setFraudScore(0);
        return user;
    }

    /**
     * Gera um número de cartão de crédito fictício com 16 dígitos.
     *
     * @return Número do cartão de crédito gerado.
     */
    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int digit = (int) (Math.random() * 10);
            cardNumber.append(digit);
        }
        return cardNumber.toString();
    }

}
