package pl.pmar.blogplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User userMock;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityContextService contextService;

    @InjectMocks
    private UserService userService;

    private User user1;
    private List<User> userList;

    @BeforeEach
    public void setup() {

        user1 = new User(1, "user1", "password1", "email1", null);
        User user2 = new User(2, "user2", "password2", "email2", null);
        User user3 = new User(3, "user2", "password3", "email3", null);
        userList = List.of(user1, user2, user3);
    }

    @Test
    public void getAllUsers_asAdmin_shouldReturnListOfUsers() {

        // given
        given(contextService.isUserAdmin()).willReturn(true);
        given(userRepository.findAll()).willReturn(userList);

        // when
        ResponseEntity<List<User>> response = userService.getAllUsers();
        List<User> body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).containsExactlyInAnyOrderElementsOf(userList);
    }

    @Test
    public void getAllUsers_asUser_shouldReturnForbidden() {

        // given
        given(contextService.isUserAdmin()).willReturn(false);

        // when
        ResponseEntity<List<User>> response = userService.getAllUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getUserById_asAuthorizedUser_shouldReturnUser() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.of(user1));

        // when
        ResponseEntity<User> response = userService.getUserById(1);
        User body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isEqualTo(user1);
    }

    @Test
    public void getUserById_asUnauthorizedUser_shouldReturnForbidden() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<User> response = userService.getUserById(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getUserById_asAdmin_butUserDontExist_shouldReturnNotFound() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<User> response = userService.getUserById(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteUser_asAuthorizedUser_shouldReturnNoContent() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.of(user1));

        // when
        ResponseEntity<Void> response = userService.deleteUser(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(postRepository).findAllByAuthorId(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    public void deleteUser_asUnauthorizedUser_shouldReturnForbidden() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<Void> response = userService.deleteUser(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        verify(postRepository, never()).findAllByAuthorId(1);
        verify(userRepository, never()).deleteById(1);
    }

    @Test
    public void deleteUser_asAdmin_butUserDontExist_shouldReturnNotFound() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Void> response = userService.deleteUser(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateUser_asAuthorizedUser_shouldReturnUser() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.of(user1));
        given(userRepository.save(user1)).willReturn(user1);

        // when
        ResponseEntity<User> response = userService.updateUser(user1);
        User body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isEqualTo(user1);
    }

    @Test
    public void updateUser_asNonAdminAuthorizedUser_shouldChangeOnlyUsernameAndEmail() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(contextService.isUserAdmin()).willReturn(false);
        given(userRepository.findById(1)).willReturn(Optional.of(userMock));

        // when
        ResponseEntity<User> response = userService.updateUser(user1);

        // then
        verify(userMock).setUsername(anyString());
        verify(userMock).setEmail(anyString());
        verify(userMock, never()).setRoles(anySet());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateUser_asAdmin_shouldChangeOnlyUsernameEmailAndRoles() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(contextService.isUserAdmin()).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.of(userMock));

        // when
        ResponseEntity<User> response = userService.updateUser(user1);

        // then
        verify(userMock).setUsername(anyString());
        verify(userMock).setEmail(anyString());
        verify(userMock).setRoles(null);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateUser_asUnauthorizedUser_shouldReturnForbidden() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<User> response = userService.updateUser(user1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void updateUser_asAdmin_butUserDontExist_shouldReturnNotFound() {

        // given
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(userRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<User> response = userService.updateUser(user1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}
