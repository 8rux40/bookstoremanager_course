package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.builder.UserDTOBuilder;
import com.tardin.bookstoremanager.users.exception.UserNotFoundException;
import com.tardin.bookstoremanager.users.mapper.UserMapper;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
}
