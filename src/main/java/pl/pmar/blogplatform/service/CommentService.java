package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.Comment;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.CommentRepository;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final SecurityContextService contextService;
    private final PostRepository postRepository;

    @Autowired
    public CommentService
            (CommentRepository commentRepository,
             SecurityContextService contextService,
             PostRepository postRepository
            ) {
        this.commentRepository = commentRepository;
        this.contextService = contextService;
        this.postRepository = postRepository;
    }


    public ResponseEntity<Comment> getCommentById(Integer id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    public ResponseEntity<Comment> addComment(Comment comment, Integer postId) {
        Optional<User> userOptional = contextService.getUserFromContext();
        Optional<Post> postOptional = postRepository.findById(postId);
        if (userOptional.isEmpty() || postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post post = postOptional.get();
        if (!contextService.isUserAuthorized(post.getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        comment.setAuthor(userOptional.get());
        comment.setCreationDate(LocalDateTime.now());
        post.getComments().add(comment);
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);

    }

    public ResponseEntity<Comment> updateComment(Comment updatedComment) {
        Optional<Comment> commentOptional = commentRepository.findById(updatedComment.getId());
        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Comment comment = commentOptional.get();

        if (!contextService.isUserAuthorized(comment.getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        comment.setContent(updatedComment.getContent());

        return ResponseEntity.ok(commentRepository.save(comment));
    }

    public ResponseEntity<Void> deleteComment(Integer id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        int authorId = commentOptional.get().getAuthor().getId();

        if (contextService.isUserAuthor(authorId)
                || contextService.isUserAdmin()
                || contextService.isUserModerator()
        ) {
            commentRepository.delete(commentOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }
}
