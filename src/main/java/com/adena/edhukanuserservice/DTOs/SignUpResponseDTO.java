package com.adena.edhukanuserservice.DTOs;

import com.adena.edhukanuserservice.models.Users;

public class SignUpResponseDTO {
    private String name;
    private String email;

    public static SignUpResponseDTO fromSignUpResponseDTO(Users dto) {
        SignUpResponseDTO dto2 = new SignUpResponseDTO();
        dto2.email = dto.getEmail();
        dto2.name = dto.getName();
        return dto2;
    }
}
