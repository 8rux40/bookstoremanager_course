package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.exception.UserNotFoundException;
import com.tardin.bookstoremanager.users.dto.MessageDTO;
import com.tardin.bookstoremanager.users.dto.UserDTO;
import com.tardin.bookstoremanager.users.entity.User;
import com.tardin.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.tardin.bookstoremanager.users.mapper.UserMapper;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final UserMapper mapper = UserMapper.INSTANCE;

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public MessageDTO create(UserDTO userToCreateDTO){
        verifyIfExists(userToCreateDTO);

        User userToCreate = mapper.toModel(userToCreateDTO);
        User createdUser = repository.save(userToCreate);

        return creationMessage(createdUser);
    }
  
    public void delete(Long id) {
        verifyIfExists(id);
        repository.deleteById(id);
    }

    private void verifyIfExists(Long id) throws UserNotFoundException {
        repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(UserDTO userToCreateDTO) {
        String email = userToCreateDTO.getEmail();
        String username = userToCreateDTO.getUsername();
        Optional<User> foundUser = repository.findByEmailOrUsername(email, username);
        if (foundUser.isPresent()){
            throw new UserAlreadyExistsException(email, username);
        }
    }

    private MessageDTO creationMessage(User createdUser) {
        String createdUsername = createdUser.getUsername();
        Long createdId = createdUser.getId();
        var userCreatedMessage = String.format("User %s with ID %s successfully created", createdUsername, createdId);
        return MessageDTO.builder()
                .message(userCreatedMessage)
                .build();
    }
}
