package pl.pmar.blogplatform.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pmar.blogplatform.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
