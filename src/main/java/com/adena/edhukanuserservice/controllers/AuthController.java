package com.adena.edhukanuserservice.controllers;

import com.adena.edhukanuserservice.DTOs.AuthResponse;
import com.adena.edhukanuserservice.DTOs.LoginRequest;
import com.adena.edhukanuserservice.DTOs.LoginRequestDTO;
import com.adena.edhukanuserservice.service.AuthService;
import com.adena.edhukanuserservice.service.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) throws Exception {

        String access_token = String.valueOf(authService.login(loginRequest));
        return ResponseEntity.ok(Map.of("access_token", access_token));
    }
}

