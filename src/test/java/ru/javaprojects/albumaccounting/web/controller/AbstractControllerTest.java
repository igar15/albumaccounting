package ru.javaprojects.albumaccounting.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.AuthorizedUser;
import ru.javaprojects.albumaccounting.UserTestData;
import ru.javaprojects.albumaccounting.util.JwtProvider;
import ru.javaprojects.albumaccounting.util.exception.ErrorType;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.javaprojects.albumaccounting.UserTestData.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    protected HttpHeaders userJwtHeader;

    protected HttpHeaders adminJwtHeader;

    @PostConstruct
    private void postConstruct() {
        AuthorizedUser authUser = new AuthorizedUser(user);
        userJwtHeader = new HttpHeaders();
        userJwtHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtProvider.generateAuthorizationToken(authUser));
        AuthorizedUser authAdmin = new AuthorizedUser(admin);
        adminJwtHeader = new HttpHeaders();
        adminJwtHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtProvider.generateAuthorizationToken(authAdmin));
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    public ResultMatcher errorType(ErrorType type) {
        return jsonPath("$.type").value(type.name());
    }

    public ResultMatcher detailMessage(String code) {
        return jsonPath("$.details").value(code);
    }
}