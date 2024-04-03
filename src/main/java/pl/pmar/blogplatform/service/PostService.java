package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.Comment;
import pl.pmar.blogplatform.model.Post;
import pl.pmar.blogplatform.model.User;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    public ResponseEntity<Post> getPostById(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Post>> getUserPosts(Integer id) {
        List<Post> posts = postRepository.findAllByUserId(id);
        return ResponseEntity.ok(posts);
    }

    public ResponseEntity<Post> createPost(Post post, Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            post.setUser(user.get());
            LocalDateTime now = LocalDateTime.now();
            post.setCreationDate(now);
            post.setModificationDate(now);
            Post savedPost =  postRepository.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    public ResponseEntity<Post> updatePost(Post post) {
        LocalDateTime now = LocalDateTime.now();
        post.setModificationDate(now);
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    public ResponseEntity<Void> deletePost(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

    }


    public ResponseEntity<List<Comment>> getPostComments(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post
                .map(p -> ResponseEntity.ok(p.getComments()))
                .orElse(ResponseEntity.notFound().build());
    }
}
