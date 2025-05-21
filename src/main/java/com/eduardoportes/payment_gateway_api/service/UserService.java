package com.eduardoportes.payment_gateway_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.eduardoportes.payment_gateway_api.config.token.record.LoginRequest;
import com.eduardoportes.payment_gateway_api.models.User;
import com.eduardoportes.payment_gateway_api.models.dto.UserDTO;
import com.eduardoportes.payment_gateway_api.repository.UserRepository;
import com.eduardoportes.payment_gateway_api.utils.UserUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public void save(UserDTO userDto){ 
        String pass = bCryptPasswordEncoder.encode(userDto.getPass());
        User user = UserUtils.dtoToUser(userDto);
        user.setPass(pass);
        repository.save(user);
    }

    public Optional<User> findByName(String username){
        return repository.findByName(username);
    }

    public Boolean isLoginCorrect(LoginRequest loginRequest, User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Boolean x = passwordEncoder.matches(loginRequest.password(), user.getPass());
        return x;
    }

}
