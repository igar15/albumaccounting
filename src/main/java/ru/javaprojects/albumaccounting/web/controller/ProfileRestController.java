package ru.javaprojects.albumaccounting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javaprojects.albumaccounting.AuthorizedUser;
import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.service.UserService;

import javax.validation.constraints.Size;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ProfileRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api/profile";

    @Autowired
    private UserService service;

    @GetMapping
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get {}", authUser.getId());
        return service.get(authUser.getId());
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestParam @Size(min = 5, max = 100) String password, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("change password for user {}", authUser.getId());
        service.changePassword(authUser.getId(), password);
    }
}