package com.hanre.fakeinsta.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hanre.fakeinsta.dto.PostDTO;
import com.hanre.fakeinsta.model.Posts;
import com.hanre.fakeinsta.model.User;
import com.hanre.fakeinsta.repository.PostRepository;
import com.hanre.fakeinsta.repository.UserRepository;
import com.hanre.fakeinsta.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<Object> uploadFile(MultipartFile file, HttpServletRequest request) {
        try {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/postImages/"));

            return ResponseUtil.dataFound("Upload Success", pic.get("url").toString(), request);
        } catch (IOException e) {
            return ResponseUtil.dataNotFound("Failed to upload the file",request);
        }
    }

    @Transactional
    public ResponseEntity<Object> save(Posts posts, HttpServletRequest request) {
        if (posts == null) return ResponseUtil.validationFailed("OBJECT NULL", null, request);
        try {
            userRepository.findById(posts.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Posts newPosts = new Posts();
            newPosts.setUserId(posts.getUserId());
            newPosts.setImageUrl(posts.getImageUrl());
            newPosts.setCaption(posts.getCaption());
            newPosts.setLikeCount(posts.getLikeCount());
            newPosts.setCommentCount(posts.getCommentCount());

            postRepository.save(newPosts);
        }catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseUtil.dataNotFound("Data invalid: " + e.getMessage(), request);
        }
        return ResponseUtil.dataSaveSuccess(request);
    }

    public ResponseEntity<Object> update(Long id, Posts posts, HttpServletRequest request) {
        Optional<Posts> postsOptional = postRepository.findById(id);
        if (postsOptional.isEmpty()) return ResponseUtil.dataNotFound("Data not found", request);
        try {
            Posts updatePost = postsOptional.get();
            updatePost.setLikeCount(posts.getLikeCount());
            updatePost.setCommentCount(posts.getCommentCount());
            postRepository.save(updatePost);
        } catch (Exception e) {
            return ResponseUtil.dataNotFound("Data invalid",request);
        }

        return ResponseUtil.dataUpdateSuccess(request);
    }

//    public ResponseEntity<Object> getUserPosts(Long id, HttpServletRequest request) {
//        try {
//            Optional<User> userOptional = userRepository.findById(id);
//            if (userOptional.isEmpty()) return ResponseUtil.dataNotFound("User not found", request);
//            List<Posts> posts = postRepository.findByUserId(userOptional.get());
//            if (!posts.isEmpty()) {
//                return ResponseUtil.dataFound("Data Found", posts, request);
//            } else {
//                return ResponseUtil.dataNotFound("Posts not Found", request);
//            }
//        } catch (Exception e) {
//            return ResponseUtil.dataNotFound("Data not Found", request);
//        }
//    }

    public ResponseEntity<Object> getAllPosts(HttpServletRequest request) {
        List<Posts> posts = postRepository.findAll();
        if (posts == null) return ResponseUtil.dataNotFound("Data not found", request);
        if (!posts.isEmpty()) {
            return ResponseUtil.dataFound("Data Found", posts, request);
        } else {
            return ResponseUtil.dataFound("List is Empty", posts, request);
        }
    }

    public Posts convertToEntity(PostDTO postDTO){
        return modelMapper.map(postDTO, Posts.class);
    }

}

