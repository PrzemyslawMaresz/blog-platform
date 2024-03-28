package pl.pmar.blogplatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private Set<Like> likes;
}
