package com.hanre.fakeinsta.repository;

import com.hanre.fakeinsta.model.Posts;
import com.hanre.fakeinsta.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.createdAt DESC")
    List<Posts> findAll();

}
