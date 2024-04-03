package pl.pmar.blogplatform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pmar.blogplatform.model.Comment;
import pl.pmar.blogplatform.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> addComment
            (@RequestBody Comment comment,
             @RequestParam Integer postId,
             @RequestParam Integer userId
            ) {
        return commentService.addComment(comment, postId, userId);
    }

    @PutMapping("/comments")
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment) {
        return commentService.updateComment(comment);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        return commentService.deleteComment(id);
    }


}
