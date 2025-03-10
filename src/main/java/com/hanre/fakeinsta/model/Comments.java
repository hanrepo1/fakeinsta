package com.hanre.fakeinsta.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "comment")
@Data
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_post",foreignKey = @ForeignKey(name = "fk_to_post"))
    private Posts postId;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

}