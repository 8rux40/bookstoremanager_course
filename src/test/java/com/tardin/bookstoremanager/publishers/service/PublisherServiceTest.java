package com.tardin.bookstoremanager.publishers.service;

import com.tardin.bookstoremanager.publishers.builder.PublisherDTOBuilder;
import com.tardin.bookstoremanager.publishers.exception.PublisherAlreadyExistsException;
import com.tardin.bookstoremanager.publishers.exception.PublisherNotFoundException;
import com.tardin.bookstoremanager.publishers.mapper.PublisherMapper;
import com.tardin.bookstoremanager.publishers.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    private final PublisherMapper mapper = PublisherMapper.INSTANCE;

    @Mock
    private PublisherRepository repository;

    @InjectMocks
    private PublisherService service;

    private PublisherDTOBuilder publisherDTOBuilder;

    @BeforeEach
    void setUp() {
        publisherDTOBuilder = PublisherDTOBuilder.builder().build();
    }

    @Test
    void whenNewPublisherIsInformedThenItShouldBeCreated() {
        var expectedPublisherToCreateDTO = publisherDTOBuilder.buildPublisherDTO();
        var expectedPublisherCreated = mapper.toModel(expectedPublisherToCreateDTO);

        when(repository.findByNameOrCode(expectedPublisherToCreateDTO.getName(), expectedPublisherToCreateDTO.getCode()))
                .thenReturn(Optional.empty());
        when(repository.save(expectedPublisherCreated)).thenReturn(expectedPublisherCreated);

        var createdPublisherDTO = service.create(expectedPublisherToCreateDTO);

        assertThat(createdPublisherDTO, is(equalTo(expectedPublisherToCreateDTO)));
    }

    @Test
    void whenExistingPublisherIsInformedThenAnExceptionShouldBeThrown() {
        var expectedPublisherToCreateDTO = publisherDTOBuilder.buildPublisherDTO();
        var expectedPublisherDuplicated = mapper.toModel(expectedPublisherToCreateDTO);

        when(repository.findByNameOrCode(expectedPublisherToCreateDTO.getName(), expectedPublisherToCreateDTO.getCode()))
                .thenReturn(Optional.of(expectedPublisherDuplicated));

        assertThrows(PublisherAlreadyExistsException.class, () -> service.create(expectedPublisherToCreateDTO));
    }

    @Test
    void whenValidIdIsGivenThenAPublisherShouldBeReturned() {
        var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        var expectedPublisherFound = mapper.toModel(expectedPublisherFoundDTO);
        final var expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        when(repository.findById(expectedPublisherFoundId)).thenReturn(Optional.of(expectedPublisherFound));

        var foundPublisherDTO = service.findById(expectedPublisherFoundId);

        assertThat(foundPublisherDTO, is(equalTo(expectedPublisherFoundDTO)));
    }

    @Test
    void whenInvalidIdIsGivenThenAnExceptionShouldBeThrown() {
        final var expectedPublisherNotFoundId = 1L;

        when(repository.findById(expectedPublisherNotFoundId)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> service.findById(expectedPublisherNotFoundId));
    }

    @Test
    void whenListPublishersIsCalledThenItShouldBeReturned() {
        var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        var expectedPublisherFound = mapper.toModel(expectedPublisherFoundDTO);

        when(repository.findAll()).thenReturn(Collections.singletonList(expectedPublisherFound));

        var publishersDTOListFound = service.findAll();

        assertThat(publishersDTOListFound, is(equalTo(Collections.singletonList(expectedPublisherFoundDTO))));
    }

    @Test
    void whenListPublishersIsCalledThenAnEmptyListShouldBeReturned() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        var publishersDTOListFound = service.findAll();

        assertThat(publishersDTOListFound.size(), is(0));
        assertThat(publishersDTOListFound, is(equalTo(Collections.emptyList())));
    }

    @Test
    void whenValidIdIsGivenThenItShouldBeDeleted() {
        var expectedPublisherToDeleteDTO = publisherDTOBuilder.buildPublisherDTO();
        final var expectedPublisherToDeleteId = expectedPublisherToDeleteDTO.getId();
        var expectedPublisherToDelete = mapper.toModel(expectedPublisherToDeleteDTO);

        doNothing().when(repository).deleteById(expectedPublisherToDeleteId);
        when(repository.findById(expectedPublisherToDeleteId)).thenReturn(Optional.of(expectedPublisherToDelete));

        service.delete(expectedPublisherToDeleteId);

        verify(repository, times(1)).deleteById(expectedPublisherToDeleteId);
        verify(repository, times(1)).findById(expectedPublisherToDeleteId);
    }

    @Test
    void whenInvalidIdIsGivenOnDeleteThenAnExceptionShouldBeThrown() {
        var expectedPublisherNotFoundId = 1L;

        when(repository.findById(expectedPublisherNotFoundId)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> service.delete(expectedPublisherNotFoundId));
    }
}
