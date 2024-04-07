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
import pl.pmar.blogplatform.repository.UserRepository;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public LikeService(
            LikeRepository likeRepository,
            PostRepository postRepository,
            UserRepository userRepository
    ) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public ResponseEntity<Like> createLike(Integer postId, Integer userId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Like like = new Like();
            like.setUser(userOptional.get());
            Post post = postOptional.get();
            post.getLikes().add(like);
            Like savedLike = likeRepository.save(like);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedLike);
        }

    }

    public ResponseEntity<Void> deleteLike(Integer id) {
        Optional<Like> likeOptional = likeRepository.findById(id);
        if (likeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            likeRepository.delete(likeOptional.get());
            return ResponseEntity.noContent().build();
        }
    }
}
