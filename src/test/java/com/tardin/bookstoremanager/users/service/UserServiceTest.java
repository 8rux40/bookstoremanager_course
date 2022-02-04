package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.builder.UserDTOBuilder;
import com.tardin.bookstoremanager.users.exception.UserNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp() {
        userDTOBuilder = UserDTOBuilder.builder().build();
    }

    @Test
    void whenValidUserIsInformedThenItShouldBeDeleted() {
        var expectedDeletedUserDTO = userDTOBuilder.buildUserDTO();
        var expectedDeletedUser = mapper.toModel(expectedDeletedUserDTO);
        Long expectedDeletedUserId = expectedDeletedUserDTO.getId();

        when(repository.findById(anyLong())).thenReturn(Optional.of(expectedDeletedUser));
        doNothing().when(repository).deleteById(anyLong());
        service.delete(expectedDeletedUserId);

        verify(repository, times(1)).deleteById(expectedDeletedUserId);
    }

    @Test
    void whenInvalidUserIdIsInformedThenAnExceptionShouldBeThrown() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.delete(0L));
    }
  
    @Test  
    void whenNewUserIsInformedThenItShouldBeCreated() {
        var expectedUserToCreateDTO = userDTOBuilder.buildUserDTO();
        var expectedUserCreated = mapper.toModel(expectedUserToCreateDTO);
        var expectedCreationMessage = "User btardin with ID 1 successfully created";
        String expectedUserEmail = expectedUserToCreateDTO.getEmail();
        String expectedUsername = expectedUserToCreateDTO.getUsername();

        when(repository.findByEmailOrUsername(expectedUserEmail, expectedUsername)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(expectedUserCreated.getPassword())).thenReturn(expectedUserCreated.getPassword());
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

    @Test
    void whenExistingUserIsInformedItShouldBeUpdated() {
        var expectedUpdatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedUpdatedUserDTO.setUsername("btardin_update");
        var expectedUpdatedUser = mapper.toModel(expectedUpdatedUserDTO);
        String expectedUpdatedMessage = "User btardin_update with ID 1 successfully updated";

        when(repository.findById(expectedUpdatedUserDTO.getId())).thenReturn(Optional.of(expectedUpdatedUser));
        when(passwordEncoder.encode(expectedUpdatedUser.getPassword())).thenReturn(expectedUpdatedUser.getPassword());
        when(repository.save(expectedUpdatedUser)).thenReturn(expectedUpdatedUser);

        MessageDTO successUpdatedMessage = service.update(expectedUpdatedUserDTO.getId(), expectedUpdatedUserDTO);

        assertThat(successUpdatedMessage.getMessage(), is(equalToObject(expectedUpdatedMessage)));
    }

    @Test
    void whenNotExistingUserIsInformedThenAnExceptionShouldBeThrown() {
        var expectedUpdatedUserDTO = userDTOBuilder.buildUserDTO();
        expectedUpdatedUserDTO.setUsername("btardin_update");

        final var expectedUpdatedUserId = expectedUpdatedUserDTO.getId();
        when(repository.findById(expectedUpdatedUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.update(expectedUpdatedUserId, expectedUpdatedUserDTO));
    }
}
