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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eduardoportes.payment_gateway_api.config.token.record.LoginRequest;
import com.eduardoportes.payment_gateway_api.config.token.record.LoginResponse;
import com.eduardoportes.payment_gateway_api.service.GeoLocationService;
import com.eduardoportes.payment_gateway_api.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping()
public class TokenController {

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private GeoLocationService geoLocationService;

    @Value("${jwt.expiration}")
    private Long expires_in;


    @PostMapping("/oauth/token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes()).getRequest();

        var userOpt = userService.findByName(loginRequest.username());
        
        if (!userOpt.isPresent() || !userService.isLoginCorrect(loginRequest, userOpt.get())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        var now = Instant.now();

        try {
            String ip = getClientIp(request);
            String[] location = geoLocationService.getLocationByIp(ip);
            
            var claims = JwtClaimsSet.builder()
                .issuer("fraud_detection_system")
                .subject(userOpt.get().getName())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expires_in))
                .claim("location", location[0] +", " + location[1])
                .build();
                var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
                return ResponseEntity.ok().body(new LoginResponse(token, expires_in));
        } catch (Exception e) {
        
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = null;

        // Try X-Forwarded-For first - this is the most common header for proxied requests
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
        // X-Forwarded-For can contain multiple IPs - we need the first one (client's IP)
        String[] ips = xForwardedFor.split(",");
        ip = ips[0].trim();
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Handle IPv6 localhost
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }


}
