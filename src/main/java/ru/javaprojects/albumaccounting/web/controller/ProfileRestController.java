package ru.javaprojects.albumaccounting.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.javaprojects.albumaccounting.AuthorizedUser;
import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.service.UserService;
import ru.javaprojects.albumaccounting.util.JwtProvider;

import javax.validation.constraints.Size;

import static ru.javaprojects.albumaccounting.util.JwtProvider.AUTHORIZATION_TOKEN_HEADER;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(name = "Profile Controller")
public class ProfileRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api/profile";

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Operation(description = "Get user profile data")
    @GetMapping
    public User get(@AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get {}", authUser.getId());
        return service.get(authUser.getId());
    }

    @Operation(description = "Change user profile password")
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestParam @Size(min = 5, max = 32) String password, @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("change password for user {}", authUser.getId());
        service.changePassword(authUser.getId(), password);
    }

    @Operation(description = "Login to app")
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        log.info("login user {}", email);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email.toLowerCase(), password));
        User loggedUser = service.getByEmail(email.toLowerCase());
        HttpHeaders jwtHeader = new HttpHeaders();
        jwtHeader.add(AUTHORIZATION_TOKEN_HEADER, jwtProvider.generateAuthorizationToken(email.toLowerCase()));
        return new ResponseEntity<>(loggedUser, jwtHeader, HttpStatus.OK);
    }
}