package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.Post;
import pl.pmar.blogplatform.repository.UserRepository;
import pl.pmar.blogplatform.model.User;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostService postService;

    @Autowired
    public UserService(UserRepository userRepository, PostService postService) {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);

    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        List<Post> userPosts = postService.getUserPosts(id);
        for (Post post : userPosts) {
            postService.deletePost(post.getId());
        }
        userRepository.deleteById(id);
    }

}
