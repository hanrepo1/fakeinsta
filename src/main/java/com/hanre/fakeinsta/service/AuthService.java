package com.hanre.fakeinsta.service;

import com.hanre.fakeinsta.dto.LoginDTO;
import com.hanre.fakeinsta.model.User;
import com.hanre.fakeinsta.repository.UserRepository;
import com.hanre.fakeinsta.security.Crypto;
import com.hanre.fakeinsta.util.JwtUtil;
import com.hanre.fakeinsta.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<Object> generateToken(LoginDTO loginDTO, HttpServletRequest request) {
        User user = userRepository.findByUsername(loginDTO.getUsername());
        if (user == null) {
            user = userRepository.findByEmail(loginDTO.getUsername());
        }
        if (user != null){
            String unlockedPassword = Crypto.performEncrypt(loginDTO.getPassword());
            if (user.getPassword().equals(unlockedPassword)){
                Map<String,Object> mapForClaims = new HashMap<>();
                mapForClaims.put("uid",user.getId());//payload
                mapForClaims.put("email",user.getEmail());//payload
                mapForClaims.put("role","user");
                String token = jwtUtil.generateToken(loginDTO.getUsername(), mapForClaims);
                return ResponseUtil.dataFound("Data Found", token, request);
            }
            return ResponseUtil.dataNotFound("Password does not match.", request);
        }
        return ResponseUtil.dataNotFound("Username not found.", request);
    }

    public ResponseEntity<Object> validateToken(String token, HttpServletRequest request) {
        boolean isValid = jwtUtil.validateToken(token);
        if (isValid){
            return ResponseUtil.dataFound("Data Found",token, request);
        } else {
            return ResponseUtil.dataNotFound("Data not found", request);
        }
    }

}