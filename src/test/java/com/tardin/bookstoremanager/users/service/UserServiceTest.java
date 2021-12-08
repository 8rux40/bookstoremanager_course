package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.builder.UserDTOBuilder;
import com.tardin.bookstoremanager.users.mapper.UserMapper;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private UserDTOBuilder userDTOBuilder;

    @BeforeEach
    void setUp(){
        userDTOBuilder = UserDTOBuilder.builder().build();
    }
}
