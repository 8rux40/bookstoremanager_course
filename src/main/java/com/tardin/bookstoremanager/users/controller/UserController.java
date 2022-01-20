package com.tardin.bookstoremanager.users.controller;

import com.tardin.bookstoremanager.users.dto.MessageDTO;
import com.tardin.bookstoremanager.users.dto.UserDTO;
import com.tardin.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }
  
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO create(@RequestBody @Valid UserDTO userDTO){
        return service.create(userDTO);
    }
  
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
