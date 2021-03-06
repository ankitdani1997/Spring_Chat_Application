package com.example.Spring_Chat_application.service;

import com.example.Spring_Chat_application.mapper.UserMapper;
import com.example.Spring_Chat_application.model.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService implements AuthenticationProvider {
    //between controller and mapper
    private UserMapper userMapper;
    private HashService hashService;

    public AuthenticationService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    //to check if the user has logged in successfully or not
    //data is received from the web form
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userMapper.getUser(username);
        if(user != null){
            String encodedSalt = user.getSalt();
            String hashedPassword = hashService.getHashedValue(password,encodedSalt);
            if(user.getPassword().equals(hashedPassword)){
                return new UsernamePasswordAuthenticationToken(username,password,new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
