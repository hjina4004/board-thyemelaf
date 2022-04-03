package com.jina.board.dto;

import lombok.Data;

@Data
public class SignupDto {
    private String username;
    private String email;
    private String password;
    private String confirmation_password;
}
