package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


}
