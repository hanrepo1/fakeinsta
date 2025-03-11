package com.hanre.fakeinsta.service;

import com.hanre.fakeinsta.dto.UserDTO;
import com.hanre.fakeinsta.model.User;
import com.hanre.fakeinsta.repository.UserRepository;
import com.hanre.fakeinsta.security.Crypto;
import com.hanre.fakeinsta.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        if (user == null) return ResponseUtil.validationFailed("OBJECT NULL", null, request);
        System.out.println(user);
        try {
            String encrypt = Crypto.performEncrypt(user.getPassword());
            user.setPassword(encrypt);

            userRepository.save(user);
        }catch (Exception e){
            return ResponseUtil.dataNotFound("Data invalid",request);
        }

        return ResponseUtil.dataSaveSuccess(request);
    }

    public ResponseEntity<Object> update(Long id, User user, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) return ResponseUtil.dataNotFound("Data not found", request);
        try {
            User updateUser = userOptional.get();
            updateUser.setEmail(user.getEmail());
            updateUser.setUsername(user.getUsername());
            String encrypt = Crypto.performEncrypt(user.getPassword());
            updateUser.setPassword(encrypt);
            userRepository.save(updateUser);
        } catch (Exception e) {
            return ResponseUtil.dataNotFound("Data invalid",request);
        }

        return ResponseUtil.dataUpdateSuccess(request);
    }

    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) return ResponseUtil.dataNotFound("Data not found", request);
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseUtil.dataNotFound("Data invalid",request);
        }

        return ResponseUtil.dataUpdateSuccess(request);
    }

    public ResponseEntity<Object> getAllUser(HttpServletRequest request) {
        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            return ResponseUtil.dataFound("Data Found", users, request);
        } else {
            return ResponseUtil.dataNotFound("Data not Found", request);
        }
    }

    public ResponseEntity<Object> getUserById(Long id, HttpServletRequest request) {
        Optional<User> users = userRepository.findById(id);
        if (users.isPresent()) {
            return ResponseUtil.dataFound("Data Found", users, request);
        } else {
            return ResponseUtil.dataNotFound("Data not Found", request);
        }
    }

    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {
        User user;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = userRepository.findByUsername(authentication.getPrincipal().toString());

        }catch (Exception e){
            return ResponseUtil.dataNotFound("Data not Found",request);
        }

        return ResponseUtil.dataFound("Data Found", user, request);
    }

    public User convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

}
