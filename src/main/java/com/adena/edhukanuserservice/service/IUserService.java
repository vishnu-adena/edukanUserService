package com.adena.edhukanuserservice.service;

import com.adena.edhukanuserservice.DTOs.PasswordChangeDTO;
import com.adena.edhukanuserservice.exceptions.InvalidPasswordException;
import com.adena.edhukanuserservice.exceptions.UserNotFoundException;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.Claims;

import java.text.ParseException;

public interface IUserService {
    boolean changePassword(PasswordChangeDTO passwordValue) throws InvalidPasswordException;

    Claims getUser(String Token) throws UserNotFoundException, ParseException, JOSEException;
}
