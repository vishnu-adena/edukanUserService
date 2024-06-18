package com.adena.edhukanuserservice.service;

import com.adena.edhukanuserservice.DTOs.PasswordChangeDTO;
import com.adena.edhukanuserservice.exceptions.InvalidPasswordException;
import com.adena.edhukanuserservice.exceptions.TokenInvalidException;
import com.adena.edhukanuserservice.exceptions.UserAlreadyPresent;
import com.adena.edhukanuserservice.exceptions.UserNotFoundException;
import com.adena.edhukanuserservice.models.Token;
import com.adena.edhukanuserservice.models.Users;
import com.adena.edhukanuserservice.respository.TokenRepository;
import com.adena.edhukanuserservice.respository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class UserService {


    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;
    private TokenRepository tokenRepository;

    @Autowired
    public  UserService(UserRepository userRepository, TokenRepository tokenRepository ,BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokenRepository = tokenRepository;

    }
    public Users signUp(String name, String email, String password) throws UserAlreadyPresent {
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        if (usersOptional.isPresent()) {
            throw new UserAlreadyPresent("User already Present With this Email "+email);
        }

        Users user = new Users();
        user.setEmail(email);
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(encoder.encode(password));
        Users savedUser =  userRepository.save(user);
        return savedUser;
    };

    public String login(String email, String password) throws UserNotFoundException, InvalidPasswordException {
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        if (usersOptional.isEmpty()) {
            throw new UserNotFoundException("User Not found SignUp");
        }
        Users users = usersOptional.get();
        if (!encoder.matches(password, users.getHashedPassword())) {
            throw new InvalidPasswordException("Wrong Password");
        }

        String userId = String.valueOf(users.getId()); // Assuming Users has an getId() method

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("name", users.getName());
        claims.put("email", users.getEmail());


        // You can add other user information to the claims as needed

        return "";
    }

//    private Key getSignKey() {
//        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    }



    public Token logout(String token) throws TokenInvalidException {
        Optional<Token>optionalToken = tokenRepository.findTokenByTokenAndExpireAtGreaterThanAndDeleted(token,new Date(),false);
        if (optionalToken.isEmpty()) {
            throw new TokenInvalidException("Token is InValid");
        }
        Token tokens = optionalToken.get();
        tokens.setDeleted(true);
        Token savedToken = tokenRepository.save(tokens);
        return savedToken;
    }

    public  Token validateToken(String token) throws TokenInvalidException {
        Optional<Token> optionalToken = tokenRepository.findTokenByTokenAndExpireAtGreaterThanAndDeleted(token,new Date(),false);
        if (optionalToken.isEmpty()) {
            throw new TokenInvalidException("Token is InValid");
        }
        return optionalToken.get();
    }

    public  boolean changePassword(PasswordChangeDTO passwordValue) throws InvalidPasswordException {

        Optional<Users> usersOptional = userRepository.findByEmail(passwordValue.getEmail());
        if (usersOptional.isEmpty()) {
            throw new InvalidPasswordException("User Not found SignUp");
        }
        Users users = usersOptional.get();
        if (!encoder.matches(passwordValue.getNewPassword(), users.getHashedPassword()) && (Objects.equals(passwordValue.getNewPassword(), passwordValue.getConfirmPassword()))) {
            throw new InvalidPasswordException("Wrong Password");
        }
        users.setHashedPassword(encoder.encode(passwordValue.getNewPassword()));

        return true;
    }
}
