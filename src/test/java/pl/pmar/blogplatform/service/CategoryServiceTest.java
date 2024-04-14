package pl.pmar.blogplatform.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.pmar.blogplatform.model.entity.Category;
import pl.pmar.blogplatform.repository.CategoryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Category categoryMock;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    public void getAllCategories_shouldReturnAllCategories() {
        // given
        given(categoryRepository.findAll()).willReturn(List.of(categoryMock));

        // when
        ResponseEntity<List<Category>> result = categoryService.getAllCategories();

        // then
        assertThat(result.getBody()).containsExactly(categoryMock);
    }

    @Test
    public void getCategoryById_shouldReturnCategory() {
        // given
        given(categoryRepository.findById(any())).willReturn(java.util.Optional.of(categoryMock));

        // when
        ResponseEntity<Category> result = categoryService.getCategoryById(1);

        // then
        assertThat(result.getBody()).isEqualTo(categoryMock);
    }

    @Test
    public void getCategoryById_butNoCategoryFound_shouldReturnNotFound() {
        // given
        given(categoryRepository.findById(any())).willReturn(java.util.Optional.empty());

        // when
        ResponseEntity<Category> result = categoryService.getCategoryById(1);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void createCategory_shouldReturnCreatedCategory() {
        // given
        given(categoryRepository.save(any())).willReturn(categoryMock);

        // when
        ResponseEntity<Category> response = categoryService.createCategory(categoryMock);
        var body = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isEqualTo(categoryMock);
    }

    @Test
    public void deleteCategory_shouldReturnNoContent() {
        // given
        given(categoryRepository.findById(any())).willReturn(java.util.Optional.of(categoryMock));

        // when
        ResponseEntity<Void> response = categoryService.deleteCategory(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(categoryRepository).deleteById(1);
    }

    @Test
    public void deleteCategory_butNoCategoryFound_shouldReturnNotFound() {
        // given
        given(categoryRepository.findById(any())).willReturn(java.util.Optional.empty());

        // when
        ResponseEntity<Void> response = categoryService.deleteCategory(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
