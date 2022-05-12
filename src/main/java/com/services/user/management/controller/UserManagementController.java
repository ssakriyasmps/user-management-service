package com.services.user.management.controller;

import com.services.user.management.model.User;
import com.services.user.management.model.UserExt;
import com.services.user.management.service.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@Slf4j
public class UserManagementController {

    private Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    private static final String SUCCESS_CREATE_MESSAGE = "{\"message\" : \"Successfully created user\", \"id\": \"%s\"}";

    @Autowired
    private UserManagementService userManagementService;

    @RequestMapping(
            value = {"/users" },
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        logger.info("get all users incoming request");
        return userManagementService.getAll();
    }

    @RequestMapping(
            value = {"/users/{userId}" },
            method = GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(final @PathVariable String userId) {
        logger.info("get user by id request");
        return userManagementService.getById(userId);
    }

    @RequestMapping(
            value = {"/users" },
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(final @Valid @RequestBody User user) {
        logger.info("add new user request {}", user);
        String id = userManagementService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format(SUCCESS_CREATE_MESSAGE, id));
    }


    @RequestMapping(
            value = {"/users/{id}" },
            method = PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addUser(final @Valid @RequestBody User user, final @PathVariable String id) {
        logger.info("update user request {}", user);
        userManagementService.update(user, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(
            value = {"/usersext" },
            method = POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserExt> addUserExt(final @Valid @RequestBody UserExt user) {
        logger.info("add new user request {}", user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(user);
    }
}
