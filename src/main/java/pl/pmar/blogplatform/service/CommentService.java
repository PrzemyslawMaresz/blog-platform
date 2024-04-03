package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.Comment;
import pl.pmar.blogplatform.model.Post;
import pl.pmar.blogplatform.model.User;
import pl.pmar.blogplatform.repository.CommentRepository;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public CommentService
            (CommentRepository commentRepository,
            UserRepository userRepository,
            PostRepository postRepository
            ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public ResponseEntity<Comment> getCommentById(Integer id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    public ResponseEntity<Comment> addComment
            (Comment comment,
             Integer postId,
             Integer userId
            ) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Post> post = postRepository.findById(postId);
        if (user.isEmpty() || post.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            comment.setUser(user.get());
            comment.setCreationDate(LocalDateTime.now());
            post.get().getComments().add(comment);
            commentRepository.save(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        }
    }

    public ResponseEntity<Comment> updateComment(Comment comment) {
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    public ResponseEntity<Void> deleteComment(Integer id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            commentRepository.delete(commentOptional.get());
            return ResponseEntity.noContent().build();
        }
    }
}
