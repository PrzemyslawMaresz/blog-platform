package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.UserRepository;

import java.util.Optional;

@Service
public class SecurityContextService {

    private final UserRepository userRepository;

    @Autowired
    public SecurityContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserFromContext() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername());
    }

    public boolean isUserAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isUserModerator() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_MODERATOR"));
    }

    public boolean isUserAuthor(Integer userId) {
        return getUserFromContext()
                .map(u -> u.getId().equals(userId))
                .orElse(false);
    }

    public boolean isUserAuthorized(Integer userId) {
        return isUserAdmin() || isUserAuthor(userId);
    }
}
