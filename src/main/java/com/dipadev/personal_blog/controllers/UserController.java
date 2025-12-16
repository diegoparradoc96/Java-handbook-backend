package com.dipadev.personal_blog.controllers;

import com.dipadev.personal_blog.dtos.UserRequestDTO;
import com.dipadev.personal_blog.dtos.UserResponseDTO;
import com.dipadev.personal_blog.services.business.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> postUser(@Valid @RequestBody UserRequestDTO userRequest) {
        UserResponseDTO createdUser = userService.postUser(userRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(createdUser.username())
                .toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

}


















