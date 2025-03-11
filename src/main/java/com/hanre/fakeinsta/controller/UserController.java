package com.hanre.fakeinsta.controller;

import com.hanre.fakeinsta.dto.UserDTO;
import com.hanre.fakeinsta.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {
        return userService.getCurrentUser(request);
    }

    @PostMapping("/createUser")
    public ResponseEntity<Object> save(@RequestBody UserDTO userDTO,
                                       HttpServletRequest request
    ){
        return userService.save(userService.convertToEntity(userDTO), request);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long id,
            @RequestBody UserDTO userDTO,
            HttpServletRequest request
    ){
        return userService.update(id, userService.convertToEntity(userDTO), request);
    }

    @PutMapping("/deleteUser/{id}")
    public ResponseEntity<Object> delete(
            @PathVariable Long id,
            HttpServletRequest request
    ){
        return userService.delete(id, request);
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<Object> getAllUser(HttpServletRequest request){

        return userService.getAllUser(request);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Object> getUserById(
            @PathVariable Long id,
            HttpServletRequest request
    ){
        return userService.getAllUser(request);
    }

}
