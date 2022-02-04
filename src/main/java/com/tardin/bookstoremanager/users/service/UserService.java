package com.tardin.bookstoremanager.users.service;

import com.tardin.bookstoremanager.users.dto.MessageDTO;
import com.tardin.bookstoremanager.users.dto.UserDTO;
import com.tardin.bookstoremanager.users.entity.User;
import com.tardin.bookstoremanager.users.exception.UserAlreadyExistsException;
import com.tardin.bookstoremanager.users.exception.UserNotFoundException;
import com.tardin.bookstoremanager.users.mapper.UserMapper;
import com.tardin.bookstoremanager.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tardin.bookstoremanager.users.utils.MessageDTOUtils.createdMessage;
import static com.tardin.bookstoremanager.users.utils.MessageDTOUtils.updatedMessage;

@Service
public class UserService {

    private static final UserMapper mapper = UserMapper.INSTANCE;

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessageDTO create(UserDTO userToCreateDTO) {
        verifyIfExists(userToCreateDTO);
        User userToCreate = mapper.toModel(userToCreateDTO);
        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));

        User createdUser = repository.save(userToCreate);
        return createdMessage(createdUser);
    }

    public MessageDTO update(Long id, UserDTO userToUpdateDTO) {
        User foundUser = verifyAndGetIfExists(id);
        userToUpdateDTO.setId(foundUser.getId());

        User userToUpdate = mapper.toModel(userToUpdateDTO);
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        userToUpdate.setCreatedDate(foundUser.getCreatedDate());

        User updatedUser = repository.save(userToUpdate);
        return updatedMessage(updatedUser);
    }

    public void delete(Long id) {
        verifyAndGetIfExists(id);
        repository.deleteById(id);
    }

    private User verifyAndGetIfExists(Long id) throws UserNotFoundException {
        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    private void verifyIfExists(UserDTO userToCreateDTO) {
        String email = userToCreateDTO.getEmail();
        String username = userToCreateDTO.getUsername();
        Optional<User> foundUser = repository.findByEmailOrUsername(email, username);
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(email, username);
        }
    }

}
