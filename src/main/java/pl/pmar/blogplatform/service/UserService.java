package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import pl.pmar.blogplatform.model.entity.User;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SecurityContextService contextService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PostRepository postRepository,
            SecurityContextService contextService
    ) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.contextService = contextService;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        if (contextService.isUserAdmin()) {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    public ResponseEntity<User> getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        if (contextService.isUserAuthorized(user.getId())) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    public ResponseEntity<Void> deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!contextService.isUserAuthorized(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Post> userPosts = postRepository.findAllByAuthorId(id);
        postRepository.deleteAll(userPosts);
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    public ResponseEntity<User> updateUser(User updatedUser) {
        Optional<User> userOptional = userRepository.findById(updatedUser.getId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOptional.get();
        if (!contextService.isUserAuthorized(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        return ResponseEntity.ok(userRepository.save(user));
    }
}
