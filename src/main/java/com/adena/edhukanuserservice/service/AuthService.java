package com.adena.edhukanuserservice.service;

import com.adena.edhukanuserservice.DTOs.LoginRequestDTO;
import com.adena.edhukanuserservice.models.Users;
import com.adena.edhukanuserservice.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public String login(LoginRequestDTO loginRequest) throws Exception{

       Optional<Users> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()){
            throw new Exception("User not found");
        }
        if(!bCryptPasswordEncoder.matches(loginRequest.getPassword(),user.get().getHashedPassword())){
            throw new Exception("Wrong password");
        }
        String response = jwtTokenProvider.generateToken(user.get().getEmail());
        return response;
    }

}
