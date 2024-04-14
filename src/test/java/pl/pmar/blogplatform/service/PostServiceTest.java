package pl.pmar.blogplatform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.pmar.blogplatform.model.entity.Comment;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.PostRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityContextService contextService;

    @Mock
    private Post postMock;

    @Mock
    private Comment commentMock;

    @Mock
    private User userMock;

    @InjectMocks
    private PostService postService;



    @Test
    public void getAllPosts_shouldReturnListOfPosts() {

        // given
        given(postRepository.findAll()).willReturn(List.of(postMock));

        // when
        ResponseEntity<List<Post>> response = postService.getAllPosts();
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).containsExactly(postMock);
    }

    @Test
    public void getPostById_shouldReturnPost() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));

        // when
        ResponseEntity<Post> response = postService.getPostById(1);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isEqualTo(postMock);
    }

    @Test
    public void getPostById_shouldReturnNotFound() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Post> response = postService.getPostById(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getUserPosts_shouldReturnListOfPosts() {

        // given
        given(postRepository.findAllByAuthorId(1)).willReturn(List.of(postMock));

        // when
        ResponseEntity<List<Post>> response = postService.getUserPosts(1);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).containsExactly(postMock);
    }

    @Test
    public void getPostComments_shouldReturnListOfComments() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(postMock.getComments()).willReturn(List.of(commentMock));

        // when
        ResponseEntity<List<Comment>> response = postService.getPostComments(1);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).containsExactly(commentMock);
    }

    @Test
    public void getPostComments_shouldReturnNotFound() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<List<Comment>> response = postService.getPostComments(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getPostByCategory_shouldReturnListOfPosts() {

        // given
        given(postRepository.findAllByCategoryId(1)).willReturn(List.of(postMock));

        // when
        ResponseEntity<List<Post>> response = postService.getPostsByCategory(1);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).containsExactly(postMock);
    }

    @Test
    public void createPost_shouldReturnCreatedPost() {

        // given
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(postRepository.save(postMock)).willReturn(postMock);

        // when
        ResponseEntity<Post> response = postService.createPost(postMock);
        var body = response.getBody();

        // then
        verify(postMock).setAuthor(userMock);
        verify(postMock).setCreationDate(any());
        verify(postMock).setModificationDate(any());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isEqualTo(postMock);
    }

    @Test
    public void createPost_byNonExistingUser_shouldReturnNotFound() {

        // given
        given(contextService.getUserFromContext()).willReturn(Optional.empty());

        // when
        ResponseEntity<Post> response = postService.createPost(postMock);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updatePost_asAuthorizedUser_shouldReturnUpdatedPost() {

        // given
        given(postMock.getId()).willReturn(1);
        given(postMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(postRepository.save(postMock)).willReturn(postMock);

        // when
        ResponseEntity<Post> response = postService.updatePost(postMock);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isEqualTo(postMock);
    }

    @Test
    public void updatePost_asUnauthorizedUser_shouldReturnForbidden() {

        // given
        given(postMock.getId()).willReturn(1);
        given(postMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<Post> response = postService.updatePost(postMock);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void deleteUser_asAuthorizedUser_shouldReturnNoContent() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(postMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthorized(1)).willReturn(true);

        // when
        ResponseEntity<Void> response = postService.deletePost(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(postRepository).deleteById(1);
    }

    @Test
    public void deleteUser_butNoUserFound_shouldReturnNotFound() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Void> response = postService.deletePost(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteUser_asUnauthorizedUser_shouldReturnForbidden() {

        // given
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(postMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<Void> response = postService.deletePost(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
