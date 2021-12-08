package com.tardin.bookstoremanager.users.controller;

import com.tardin.bookstoremanager.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserControllerDocs {

    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }
}
