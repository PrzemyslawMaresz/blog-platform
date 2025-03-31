package pl.pmar.blogplatform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.pmar.blogplatform.model.entity.Like;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.model.entity.User;
import pl.pmar.blogplatform.repository.LikeRepository;
import pl.pmar.blogplatform.repository.PostRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityContextService contextService;

    @Mock
    private Post postMock;

    @Mock
    private User userMock;

    @Mock
    private User authorMock;

    @Mock
    private Like likeMock;

    @InjectMocks
    private LikeService likeService;


    @Test
    public void createLike_asUser_shouldReturnLike() {
        // given
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(postMock.getLikes()).willReturn(new ArrayList<>());
        given(likeRepository.save(any(Like.class))).willReturn(likeMock);

        // when
        ResponseEntity<Like> response = likeService.createLike(1);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isInstanceOf(Like.class);
    }

    @Test
    public void createLike_asUserButAlreadyLiked_shouldReturnConflict() {
        // given
        given(postRepository.findById(1)).willReturn(Optional.of(postMock));
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(likeRepository.existsByPostIdAndUserId(1, 1)).willReturn(true);

        // when
        ResponseEntity<Like> response = likeService.createLike(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }


    @Test
    public void createLike_butPostNotFound_shouldReturnNotFound() {
        // given
        given(postRepository.findById(1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Like> response = likeService.createLike(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteLike_asUser_shouldReturnNoContent() {
        // given
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(userMock.getId()).willReturn(1);
        given(likeRepository.findByPostIdAndUserId(1, 1)).willReturn(Optional.of(likeMock));

        // when
        ResponseEntity<Void> response = likeService.deleteLikeByPostId(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void deleteLike_asNotLikeAuthor_shouldReturnForbidden() {
        // given
        given(contextService.getUserFromContext()).willReturn(Optional.of(userMock));
        given(userMock.getId()).willReturn(1);
        given(likeRepository.findByPostIdAndUserId(1, 1)).willReturn(Optional.empty());

        // when
        ResponseEntity<Void> response = likeService.deleteLikeByPostId(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
