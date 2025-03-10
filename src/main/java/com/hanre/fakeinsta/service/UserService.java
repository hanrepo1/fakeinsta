package com.hanre.fakeinsta.service;

import com.hanre.fakeinsta.dto.UserDTO;
import com.hanre.fakeinsta.model.User;
import com.hanre.fakeinsta.repository.UserRepository;
import com.hanre.fakeinsta.security.Crypto;
import com.hanre.fakeinsta.util.ResponseUtil;
import com.hanre.fakeinsta.util.TransformToDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransformToDTO transformToDTO;

    public ResponseEntity<Object> save(User user, HttpServletRequest request) {
        if (user == null) return ResponseUtil.validationFailed("OBJECT NULL", null, request);
        System.out.println(user);
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String encrypt = Crypto.performEncrypt(user.getPassword());
            user.setPassword(encrypt);
            user.setCreatedBy(authentication.getPrincipal().toString());

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

    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<User> page = null;
        List<User> list = null;
        try{
            page = userRepository.findAll(pageable);
            list = page.getContent();
            if(list.isEmpty()){
                return ResponseUtil.dataNotFound("Data not found", request);
            }
        }catch (Exception e){
            return ResponseUtil.dataNotFound("FE001003031",request);
        }

        return transformToDTO.
                transformObject(new HashMap<>(),
                        list, page,null,null,null ,request);
    }

    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) {
        User user = new User();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            user = userRepository.findByUsername(authentication.getPrincipal().toString());

        }catch (Exception e){
            return ResponseUtil.dataNotFound("Data not Found",request);
        }

        return ResponseUtil.dataFound("Data Found", user, request);
    }

    public ResponseEntity<Object> updateUserRole (HttpServletRequest request, Long id, String roleName) {
        Optional<User> userOptional = userRepository.findById(id);
        Optional<Roles> rolesOptional = rolesRepository.findByName(roleName.toUpperCase());
        if (userOptional.isEmpty()) return ResponseUtil.dataNotFound("User not found", request);
        if (rolesOptional.isEmpty()) return ResponseUtil.dataNotFound("Role not found", request);
        try {
            User updateUser = userOptional.get();
            updateUser.setRoleId(rolesOptional.get());
            userRepository.save(updateUser);
        } catch (Exception e) {
            return ResponseUtil.dataNotFound("Data invalid",request);
        }

        return ResponseUtil.dataUpdateSuccess(request);
    }

    public User convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

}
