package com.adena.edhukanuserservice.controllers;

import com.adena.edhukanuserservice.DTOs.*;
import com.adena.edhukanuserservice.exceptions.InvalidPasswordException;
import com.adena.edhukanuserservice.exceptions.TokenInvalidException;
import com.adena.edhukanuserservice.exceptions.UserAlreadyPresent;
import com.adena.edhukanuserservice.exceptions.UserNotFoundException;
import com.adena.edhukanuserservice.models.Token;
import com.adena.edhukanuserservice.models.Users;
import com.adena.edhukanuserservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://localhost:3000")
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Users signUp(@RequestBody SignupRequestDTO signupRequestDTO) throws UserAlreadyPresent {

        Users user = userService.signUp(signupRequestDTO.getName(), signupRequestDTO.getEmail(), signupRequestDTO.getPassword());
        SignUpResponseDTO signUpResponseDTO = SignUpResponseDTO.fromSignUpResponseDTO(user);
        return user;

    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) throws UserNotFoundException, InvalidPasswordException {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        String token = userService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        loginResponseDTO.setToken(token);
        loginResponseDTO.setMessage("Successfully logged in");
        return loginResponseDTO;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody LogoutRequestDTO requestDTO) throws TokenInvalidException {

        Token token = userService.logout(requestDTO.getToken());
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                token.isDeleted()==true ?"Successfully logged out":"Invalid token",
                token.isDeleted()==true ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
        return responseEntity;
    }

    @GetMapping("/validate/{tokenValue}")
    public Token validateToken(@PathVariable String tokenValue) throws TokenInvalidException {
        Token token = userService.validateToken(tokenValue);
        //UserDTO userDTO = UserDTO.fromUser(token.getUser());
        return token;
    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) throws InvalidPasswordException {
        boolean response = userService.changePassword(passwordChangeDTO);
        if (response)return "Successfully changed password";
        else return "Invalid password";
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }


}
