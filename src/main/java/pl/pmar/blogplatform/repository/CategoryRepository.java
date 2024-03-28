package pl.pmar.blogplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pmar.blogplatform.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
