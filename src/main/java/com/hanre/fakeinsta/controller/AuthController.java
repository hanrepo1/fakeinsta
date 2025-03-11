package com.hanre.fakeinsta.controller;

import com.hanre.fakeinsta.dto.LoginDTO;
import com.hanre.fakeinsta.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<Object> generateToken(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        return authService.generateToken(loginDTO, request);
    }

    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(HttpServletRequest request, @RequestParam String token) {
        return authService.validateToken(token, request);
    }

}