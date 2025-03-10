package com.hanre.fakeinsta.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "post")
@Data
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;
    private String displayName;
    private String description;

    @OneToMany
    @JoinColumn(name = "id_post",foreignKey = @ForeignKey(name = "fk_to_post"))
    private Comments comments;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

}
