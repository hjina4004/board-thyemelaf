package com.jina.board.controller;

import com.jina.board.dto.SignupDto;
import com.jina.board.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("auth")
@RequiredArgsConstructor
@Controller
public class AuthController {
    private final UserService userService;

    @GetMapping("login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("register")
    public String signup(SignupDto signupDto) {
        userService.Signup(signupDto);

        return "redirect:/";
    }
}
