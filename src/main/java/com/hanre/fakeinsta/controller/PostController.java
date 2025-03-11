package com.hanre.fakeinsta.controller;

import com.hanre.fakeinsta.dto.PostDTO;
import com.hanre.fakeinsta.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/createPost")
    public ResponseEntity<Object> save(@RequestBody PostDTO postDTO,
                                       HttpServletRequest request
    ){
        return postService.save(postService.convertToEntity(postDTO), request);
    }

    @PutMapping("/updatePost/{id}")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long id,
            @RequestBody PostDTO postDTO,
            HttpServletRequest request
    ){
        return postService.update(id, postService.convertToEntity(postDTO), request);
    }

//    @GetMapping("/getUserPosts/{id}")
//    public ResponseEntity<Object> getUserPosts(
//            @PathVariable(value = "id") Long id,
//            HttpServletRequest request
//    ){
//        return postService.getUserPosts(id, request);
//    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<Object> getAllPosts(HttpServletRequest request){
        return postService.getAllPosts(request);
    }

    @PostMapping("/uploadPhoto")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        return postService.uploadFile(file, request);
    }

}