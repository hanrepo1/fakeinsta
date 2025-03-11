package com.hanre.fakeinsta.repository;

import com.hanre.fakeinsta.model.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

    @Query("SELECT c FROM Comments c WHERE c.postId = :postId ORDER BY c.createdAt DESC")
    List<Comments> findByPostId(Long id);

}