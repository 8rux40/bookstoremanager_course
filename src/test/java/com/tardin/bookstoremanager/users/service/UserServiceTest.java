package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.builder.UserDTOBuilder;
import com.tardin.bookstoremanager.users.dto.MessageDTO;
import com.tardin.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.tardin.bookstoremanager.users.mapper.UserMapper;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenNewUserIsInformedThenItShouldBeCreated() {
        var expectedUserToCreateDTO = userDTOBuilder.buildUserDTO();
        var expectedUserCreated = mapper.toModel(expectedUserToCreateDTO);
        var expectedCreationMessage = "User btardin with ID 1 successfully created";
        String expectedUserEmail = expectedUserToCreateDTO.getEmail();
        String expectedUsername = expectedUserToCreateDTO.getUsername();

        when(repository.findByEmailOrUsername(expectedUserEmail, expectedUsername)).thenReturn(Optional.empty());
        when(repository.save(expectedUserCreated)).thenReturn(expectedUserCreated);
        MessageDTO creationMessage = service.create(expectedUserToCreateDTO);

        assertThat(expectedCreationMessage, is(equalTo(creationMessage.getMessage())));
    }

    @Test
    void whenExistingUserIsInformedThenAnExceptionShouldBeThrown() {
        var expectedDuplicatedUserDTO = userDTOBuilder.buildUserDTO();
        var expectedDuplicatedUser = mapper.toModel(expectedDuplicatedUserDTO);
        String expectedUserEmail = expectedDuplicatedUserDTO.getEmail();
        String expectedUsername = expectedDuplicatedUserDTO.getUsername();

        when(repository.findByEmailOrUsername(expectedUserEmail, expectedUsername))
                .thenReturn(Optional.of(expectedDuplicatedUser));

        assertThrows(UserAlreadyExistsException.class, () -> service.create(expectedDuplicatedUserDTO));
    }
}
