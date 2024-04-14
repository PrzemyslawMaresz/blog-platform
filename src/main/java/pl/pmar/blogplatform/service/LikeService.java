package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.Like;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.LikeRepository;
import pl.pmar.blogplatform.repository.PostRepository;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final SecurityContextService contextService;

    @Autowired
    public LikeService(
            LikeRepository likeRepository,
            PostRepository postRepository,
            SecurityContextService contextService
    ) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.contextService = contextService;
    }


    public ResponseEntity<Like> createLike(Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = contextService.getUserFromContext();

        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        int postAuthorId = postOptional.get().getAuthor().getId();
        if (! contextService.isUserAuthor(postAuthorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Like like = new Like();
        like.setUser(userOptional.get());
        Post post = postOptional.get();
        post.getLikes().add(like);
        Like savedLike = likeRepository.save(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);

    }

    public ResponseEntity<Void> deleteLike(Integer likeId) {
        Optional<Like> likeOptional = likeRepository.findById(likeId);
        if (likeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Like like = likeOptional.get();

        Optional<User> userOptional = contextService.getUserFromContext();
        Optional<Post> postOptional = postRepository.findPostByLikesId(like.getId());
        if (userOptional.isEmpty() || postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        int postAuthorId = postOptional.get().getAuthor().getId();
        if (! contextService.isUserAuthor(postAuthorId) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        likeRepository.delete(like);

        return ResponseEntity.noContent().build();

    }
}
