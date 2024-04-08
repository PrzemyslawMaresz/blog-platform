package pl.pmar.blogplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.pmar.blogplatform.model.entity.RefreshToken;
import pl.pmar.blogplatform.model.entity.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(User user);
}
