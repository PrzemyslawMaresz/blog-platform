package pl.pmar.blogplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pmar.blogplatform.model.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
}
