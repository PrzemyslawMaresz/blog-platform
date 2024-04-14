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
import pl.pmar.blogplatform.repository.CommentRepository;
import pl.pmar.blogplatform.repository.PostRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityContextService contextService;

    @Mock
    private Comment commentMock;

    @Mock
    private User userMock;

    @Mock
    private Post postMock;

    @InjectMocks
    private CommentService commentService;


    @Test
    public void getCommentById_ShouldReturnComment() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));

        // when
        ResponseEntity<Comment> response = commentService.getCommentById(1);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isEqualTo(commentMock);
    }

    @Test
    public void getCommentById_butNoCommentFound_ShouldReturnNotFound() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Comment> response = commentService.getCommentById(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addComment_asAuthorizedUser_ShouldReturnCreated() {

        // given
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(postMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(commentRepository.save(commentMock)).willReturn(commentMock);

        // when
        ResponseEntity<Comment> response = commentService.addComment(commentMock, 1);
        var body = response.getBody();

        // then
        verify(commentMock).setAuthor(userMock);
        verify(commentMock).setCreationDate(any());
        verify(postMock).getComments();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isEqualTo(commentMock);
    }

    @Test
    public void addComment_asUnauthorizedUser_ShouldReturnForbidden() {

        // given
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(postMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<Comment> response = commentService.addComment(commentMock, 1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void addComment_butPostNotFound_ShouldReturnNotFound() {

        // given
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(postRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Comment> response = commentService.addComment(commentMock, 1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addComment_butUserNotFound_ShouldReturnNotFound() {

        // given
        given(contextService.getUserFromContext()).willReturn(Optional.empty());
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));

        // when
        ResponseEntity<Comment> response = commentService.addComment(commentMock, 1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateComment_asAuthorizedUser_ShouldReturnUpdatedComment() {

        // given
        given(commentMock.getId()).willReturn(1);
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));
        given(commentMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthorized(1)).willReturn(true);
        given(commentMock.getContent()).willReturn("content");
        given(commentRepository.save(commentMock)).willReturn(commentMock);

        // when
        ResponseEntity<Comment> response = commentService.updateComment(commentMock);
        var body = response.getBody();

        // then
        verify(commentMock).setContent(anyString());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isEqualTo(commentMock);
    }

    @Test
    public void updateComment_asUnauthorizedUser_ShouldReturnForbidden() {

        // given
        given(commentMock.getId()).willReturn(1);
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));
        given(commentMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthorized(1)).willReturn(false);

        // when
        ResponseEntity<Comment> response = commentService.updateComment(commentMock);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void updateComment_butCommentNotFound_ShouldReturnNotFound() {

        // given
        given(commentMock.getId()).willReturn(1);
        given(commentRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Comment> response = commentService.updateComment(commentMock);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteComment_asCommentAuthor_ShouldReturnNoContent() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));
        given(commentMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthor(1)).willReturn(true);

        // when
        ResponseEntity<Void> response = commentService.deleteComment(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteComment_asAdmin_ShouldReturnNoContent() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));
        given(commentMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAdmin()).willReturn(true);


        // when
        ResponseEntity<Void> response = commentService.deleteComment(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteComment_asModerator_ShouldReturnNoContent() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));
        given(commentMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserModerator()).willReturn(true);


        // when
        ResponseEntity<Void> response = commentService.deleteComment(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteComment_asUnauthorizedUser_ShouldReturnForbidden() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.of(commentMock));
        given(commentMock.getAuthor()).willReturn(userMock);
        given(userMock.getId()).willReturn(1);
        given(contextService.isUserAuthor(1)).willReturn(false);
        given(contextService.isUserAdmin()).willReturn(false);
        given(contextService.isUserModerator()).willReturn(false);

        // when
        ResponseEntity<Void> response = commentService.deleteComment(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }



    @Test
    public void deleteComment_butCommentNotFound_ShouldReturnNotFound() {

        // given
        given(commentRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Void> response = commentService.deleteComment(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


}
