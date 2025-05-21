package com.eduardoportes.payment_gateway_api.config.token.record;

public record LoginResponse(String token, Long expires_in) {
}
