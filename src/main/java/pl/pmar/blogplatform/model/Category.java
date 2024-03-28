package pl.pmar.blogplatform.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Post> posts;
}
