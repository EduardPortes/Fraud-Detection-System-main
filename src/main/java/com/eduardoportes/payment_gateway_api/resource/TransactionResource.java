package com.eduardoportes.payment_gateway_api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduardoportes.payment_gateway_api.models.TransactionRequest;
import com.eduardoportes.payment_gateway_api.models.TransactionResponse;
import com.eduardoportes.payment_gateway_api.service.TransactionService;


@RestController
@RequestMapping("/transaction")
public class TransactionResource {

    @Autowired
    private TransactionService service;

    @PostMapping()
    public ResponseEntity<TransactionResponse> create(@RequestBody TransactionRequest request) {

        TransactionResponse response = service.create(request);

        if(response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().body(response);
    }

}
