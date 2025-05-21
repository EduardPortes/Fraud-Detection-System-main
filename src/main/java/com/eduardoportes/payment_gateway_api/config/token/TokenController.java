package com.eduardoportes.payment_gateway_api.config.token;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduardoportes.payment_gateway_api.config.token.record.LoginRequest;
import com.eduardoportes.payment_gateway_api.config.token.record.LoginResponse;
import com.eduardoportes.payment_gateway_api.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping()
public class TokenController {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserService userService;

    @Value("${jwt.expiration}")
    private Long expires_in;


    @PostMapping("/oauth/token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        var userOpt = userService.findByName(loginRequest.username());
        
        if (!userOpt.isPresent() || !userService.isLoginCorrect(loginRequest, userOpt.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
            .issuer("fraud_detection_system")
            .subject(userOpt.get().getName())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expires_in))
            .build();
        
        var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok().body(new LoginResponse(token, expires_in));
    }



}
