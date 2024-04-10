package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.Comment;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.PostRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final SecurityContextService contextService;

    @Autowired
    public PostService(
            PostRepository postRepository,
            SecurityContextService securityContextService
    ) {
        this.postRepository = postRepository;
        this.contextService = securityContextService;
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
        List<Post> posts = postRepository.findAllByAuthorId(id);
        return ResponseEntity.ok(posts);
    }

    public ResponseEntity<Post> createPost(Post post) {
        Optional<User> user = contextService.getUserFromContext();
        if (user.isPresent()) {
            post.setAuthor(user.get());
            LocalDateTime now = LocalDateTime.now();
            post.setCreationDate(now);
            post.setModificationDate(now);
            Post savedPost =  postRepository.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    public ResponseEntity<Post> updatePost(Post updatedPost) {
        Optional<Post> postOptional = postRepository.findById(updatedPost.getId());
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = postOptional.get();

        if (! contextService.isUserAuthorized(post.getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        post.setModificationDate(LocalDateTime.now());
        post.setTitle(updatedPost.getTitle());
        post.setContent(updatedPost.getContent());
        post.setCategory(updatedPost.getCategory());

        return ResponseEntity.ok(postRepository.save(post));
    }

    public ResponseEntity<Void> deletePost(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (! contextService.isUserAuthorized(post.get().getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        postRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }


    public ResponseEntity<List<Comment>> getPostComments(Integer id) {
        Optional<Post> post = postRepository.findById(id);
        return post
                .map(p -> ResponseEntity.ok(p.getComments()))
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Post>> getPostsByCategory(Integer id) {
        List<Post> posts = postRepository.findAllByCategoryId(id);
        return ResponseEntity.ok(posts);
    }
}
