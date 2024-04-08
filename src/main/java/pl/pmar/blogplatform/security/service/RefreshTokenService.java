package pl.pmar.blogplatform.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.RefreshToken;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.RefreshTokenRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import pl.pmar.blogplatform.security.exception.TokenRefreshException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${blog-platform.security.jwt.refresh.expiration}")
    private String refreshTokenDurationStr;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }


    public RefreshToken createRefreshToken(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        deleteTokenByUserIfExists(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        long refreshTokenDuration = Long.parseLong(refreshTokenDurationStr);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDuration));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public void deleteTokenByUserIfExists(User user) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);
        refreshToken.ifPresent(refreshTokenRepository::delete);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (isTokenExpired(token)) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().compareTo(Instant.now()) < 0;
    }
}
