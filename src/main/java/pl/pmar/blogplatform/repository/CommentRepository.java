package pl.pmar.blogplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pmar.blogplatform.model.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
