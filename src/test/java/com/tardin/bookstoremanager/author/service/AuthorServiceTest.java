package com.tardin.bookstoremanager.author.service;

import com.tardin.bookstoremanager.author.builder.AuthorDTOBuilder;
import com.tardin.bookstoremanager.author.exception.AuthorAlreadyExistsException;
import com.tardin.bookstoremanager.author.exception.AuthorNotFoundException;
import com.tardin.bookstoremanager.author.mapper.AuthorMapper;
import com.tardin.bookstoremanager.author.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    private final AuthorMapper authorMapper = AuthorMapper.INSTANCE;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private AuthorDTOBuilder authorDTOBuilder;

    @BeforeEach
    void setUp() {
        authorDTOBuilder = AuthorDTOBuilder.builder().build();
    }

    @Test
    void whenNewAuthorIsInformedThenItShouldBeCreated() {
        var expectedAuthorToCreateDTO = authorDTOBuilder.buildAuthorDTO();
        var expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreateDTO);

        when(authorRepository.save(expectedCreatedAuthor)).thenReturn(expectedCreatedAuthor);
        when(authorRepository.findByName(expectedAuthorToCreateDTO.getName())).thenReturn(Optional.empty());

        var createdAuthorDTO = authorService.create(expectedAuthorToCreateDTO);
        assertThat(createdAuthorDTO, is(equalTo(expectedAuthorToCreateDTO)));
    }

    @Test
    void whenExistingAuthorIsInformedThenAnExceptionShouldBeThrown() {
        var expectedAuthorToCreateDTO = authorDTOBuilder.buildAuthorDTO();
        var expectedCreatedAuthor = authorMapper.toModel(expectedAuthorToCreateDTO);

        when(authorRepository.findByName(expectedAuthorToCreateDTO.getName())).thenReturn(Optional.of(expectedCreatedAuthor));

        assertThrows(AuthorAlreadyExistsException.class, () -> authorService.create(expectedAuthorToCreateDTO));
    }

    @Test
    void whenValidIdIsGivenThenAnAuthorShouldBeReturned() {
        var expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        var expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        when(authorRepository.findById(expectedFoundAuthorDTO.getId())).thenReturn(Optional.of(expectedFoundAuthor));
        var foundAuthorDTO = authorService.findById(expectedFoundAuthorDTO.getId());

        assertThat(foundAuthorDTO, is(equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        var expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();

        when(authorRepository.findById(expectedFoundAuthorDTO.getId())).thenReturn(Optional.empty());
        var id = expectedFoundAuthorDTO.getId();

        assertThrows(AuthorNotFoundException.class, () -> authorService.findById(id));
    }

    @Test
    void whenListAuthorsIsCalledThenItShouldBeReturned() {
        var expectedFoundAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        var expectedFoundAuthor = authorMapper.toModel(expectedFoundAuthorDTO);

        when(authorRepository.findAll()).thenReturn(Collections.singletonList(expectedFoundAuthor));
        var foundAuthorsDTO = authorService.findAll();

        assertThat(foundAuthorsDTO.size(), is(1));
        assertThat(foundAuthorsDTO.get(0), is(equalTo(expectedFoundAuthorDTO)));
    }

    @Test
    void whenListAuthorsIsCalledThenAnEmptyListShouldBeReturned() {
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());
        var foundAuthorsDTO = authorService.findAll();

        assertThat(foundAuthorsDTO.size(), is(0));
    }

    @Test
    void whenValidIdIsGivenThenItShouldBeDeleted() {
        var expectedDeletedAuthorDTO = authorDTOBuilder.buildAuthorDTO();
        var expectedDeletedAuthor = authorMapper.toModel(expectedDeletedAuthorDTO);

        Long expectedDeletedAuthorId = expectedDeletedAuthor.getId();
        doNothing().when(authorRepository).deleteById(expectedDeletedAuthorId);
        when(authorRepository.findById(expectedDeletedAuthorId)).thenReturn(Optional.of(expectedDeletedAuthor));

        authorService.delete(expectedDeletedAuthorId);

        verify(authorRepository, times(1)).deleteById(expectedDeletedAuthorId);
        verify(authorRepository, times(1)).findById(expectedDeletedAuthorId);
    }

    @Test
    void whenInvalidIdIsGivenOnDeleteThenAnExceptionShouldBeThrown() {
        var expectedInvalidAuthorId = 1L;

        when(authorRepository.findById(expectedInvalidAuthorId)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> authorService.delete(expectedInvalidAuthorId));
    }
}
