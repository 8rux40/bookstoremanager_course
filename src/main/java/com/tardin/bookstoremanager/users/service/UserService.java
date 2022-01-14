package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.exception.UserNotFoundException;
import com.tardin.bookstoremanager.users.mapper.UserMapper;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final UserMapper mapper = UserMapper.INSTANCE;

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void delete(Long id) {
        verifyIfExists(id);
        repository.deleteById(id);
    }

    private void verifyIfExists(Long id) throws UserNotFoundException{
        repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
