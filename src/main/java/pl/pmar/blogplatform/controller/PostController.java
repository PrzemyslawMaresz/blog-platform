package pl.pmar.blogplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pmar.blogplatform.model.entity.Comment;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Integer id) {
        return postService.getUserPosts(id);
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @RequestParam Integer userId) {
        return postService.createPost(post, userId);
    }

    @PutMapping("/posts")
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        return postService.deletePost(id);
    }

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<List<Comment>> getPostComments(@PathVariable Integer id) {
        return postService.getPostComments(id);
    }
}
