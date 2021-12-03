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
import static org.mockito.Mockito.when;

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
        var expectedPublisherFoundDTO = publisherDTOBuilder.buildPublisherDTO();
        final var expectedPublisherFoundId = expectedPublisherFoundDTO.getId();

        when(repository.findById(expectedPublisherFoundId)).thenReturn(Optional.empty());

        assertThrows(PublisherNotFoundException.class, () -> service.findById(expectedPublisherFoundId));
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
}
