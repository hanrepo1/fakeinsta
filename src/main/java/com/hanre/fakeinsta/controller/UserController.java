package com.hanre.fakeinsta.controller;

import com.hanre.fakeinsta.dto.UserDTO;
import com.hanre.fakeinsta.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    private Map<String,Object> mapSorting = new HashMap<>();
    private final String defaultSortingColumnGroupMenu = "id";

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

    @GetMapping("/findUser/all/{page}/{sort}/{sort-by}")
    public ResponseEntity<Object> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,//page yang ke ?
            @RequestParam(value = "sort", defaultValue = "asc") String sort,//asc desc
            @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,// column Name in java Variable,
            @RequestParam("size") Integer size,
            HttpServletRequest request
    ){
        page = (page==null)?0:page;
        /** function yang bersifat global di paging , untuk memberikan default jika data request tidak mengirim format sort dengan benar asc/desc */
        Pageable pageable;
        if (sort.equalsIgnoreCase("asc")){
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }
        return userService.findAll(pageable,request);
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<Object> updateRole(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestParam String roleName
    ) {
        return userService.updateUserRole(request, id, roleName);
    }
}
