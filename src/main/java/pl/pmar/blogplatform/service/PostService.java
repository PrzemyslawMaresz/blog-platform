package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.Post;
import pl.pmar.blogplatform.model.User;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Integer id) {
        return postRepository.findById(id).orElse(null);
    }

    public Post createPost(Post post, Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        post.setUser(user);
        LocalDateTime now = LocalDateTime.now();
        post.setCreationDate(now);
        post.setModificationDate(now);
        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        LocalDateTime now = LocalDateTime.now();
        post.setModificationDate(now);
        return postRepository.save(post);
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    public List<Post> getUserPosts(Integer id) {
        return postRepository.findAllByUserId(id);
    }
}
