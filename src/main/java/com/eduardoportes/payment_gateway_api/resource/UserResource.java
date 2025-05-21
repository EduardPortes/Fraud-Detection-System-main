package com.eduardoportes.payment_gateway_api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eduardoportes.payment_gateway_api.models.dto.UserDTO;
import com.eduardoportes.payment_gateway_api.service.UserService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserService  userService;

    @ApiOperation("Create an User")
    @PostMapping("/signup")
    public ResponseEntity<Void> create(@RequestBody UserDTO userDTO) {
        try {
            userService.save(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    
}
