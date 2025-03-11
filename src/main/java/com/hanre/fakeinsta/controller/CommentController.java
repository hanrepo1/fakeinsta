package com.hanre.fakeinsta.controller;

import com.hanre.fakeinsta.dto.CommentDTO;
import com.hanre.fakeinsta.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<Object> save(@RequestBody CommentDTO commentDTO,
                                       HttpServletRequest request
    ){
        return commentService.save(commentService.convertToEntity(commentDTO), request);
    }

    @GetMapping("/getAllComments/{id}")
    public ResponseEntity<Object> getAllCommentsById(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request
    ){
        return commentService.getAllCommentsById(id, request);
    }

}