package ru.javaprojects.albumaccounting.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.service.UserService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ProfileRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api/profile";
    static final int STUB_ID = 100000;

    @Autowired
    private UserService service;

    @GetMapping
    public User get() {
        log.info("get {}", STUB_ID);
        return service.get(STUB_ID);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestParam @Size(min = 5, max = 100) String password) {
        log.info("change password for user {}", STUB_ID);
        service.changePassword(STUB_ID, password);
    }
}