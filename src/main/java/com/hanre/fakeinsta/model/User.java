package com.hanre.fakeinsta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true,length = 50)
    private String email;

    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_post",foreignKey = @ForeignKey(name = "fk_to_post"))
    private Posts postId;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private String createdBy;

}
