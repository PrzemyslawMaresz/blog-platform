package pl.pmar.blogplatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE , orphanRemoval = true)
    private Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE , orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE , orphanRemoval = true)
    private Set<Like> likes;
}
