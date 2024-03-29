package pl.pmar.blogplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.pmar.blogplatform.model.Post;
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
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/posts/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> getUserPosts(@PathVariable Integer id) {
        return postService.getUserPosts(id);
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody Post post, @RequestParam Integer userId) {
        return postService.createPost(post, userId);
    }

    @PutMapping("/posts")
    public Post updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
    }
}
