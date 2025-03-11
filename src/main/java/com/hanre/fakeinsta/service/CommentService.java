package com.hanre.fakeinsta.service;

import com.hanre.fakeinsta.dto.CommentDTO;
import com.hanre.fakeinsta.model.Comments;
import com.hanre.fakeinsta.repository.CommentRepository;
import com.hanre.fakeinsta.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<Object> save(Comments comments, HttpServletRequest request) {
        if (comments == null) return ResponseUtil.validationFailed("OBJECT NULL", null, request);
        System.out.println(comments);
        try {
            commentRepository.save(comments);
        }catch (Exception e){
            return ResponseUtil.dataNotFound("Data invalid",request);
        }

        return ResponseUtil.dataSaveSuccess(request);
    }

    public ResponseEntity<Object> getAllCommentsById(Long id, HttpServletRequest request) {
        List<Comments> comments = commentRepository.findByPostId(id);
        if (!comments.isEmpty()) {
            return ResponseUtil.dataFound("Data Found", comments, request);
        } else {
            return ResponseUtil.dataNotFound("Data not Found", request);
        }
    }

    public Comments convertToEntity(CommentDTO commentDTO){
        return modelMapper.map(commentDTO, Comments.class);
    }

}
