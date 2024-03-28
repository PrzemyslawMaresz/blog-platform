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
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        userRepository.deleteById(id);
    }

    public List<Post> getUserPosts(Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user.getPosts().stream().toList();
        } else {
            return Collections.emptyList();
        }
    }
}
