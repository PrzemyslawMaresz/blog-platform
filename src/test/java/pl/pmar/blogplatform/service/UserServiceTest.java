package pl.pmar.blogplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityContextService contextService;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private User user3;
    private List<User> userList;

    @BeforeEach
    public void setup() {

        user1 = new User(1, "user1", "password1", "email1", null);
        user2 = new User(2, "user2", "password2", "email2", null);
        user3 = new User(3, "user3", "password3", "email3", null);
        userList = List.of(user1, user2, user3);
    }

    @Test
    public void getAllUsers_asAdmin_shouldReturnListOfUsers() {

        // given
        given(contextService.isUserAdmin()).willReturn(true);
        given(userRepository.findAll()).willReturn(userList);

        // when
        List<User> result = userService.getAllUsers().getBody();

        // then
        assertThat(result).containsExactlyInAnyOrderElementsOf(userList);
    }
}
