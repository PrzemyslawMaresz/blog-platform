package pl.pmar.blogplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pmar.blogplatform.model.entity.Like;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByPostIdAndUserId(Integer postId, Integer userId);
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
}
