package ru.javaprojects.albumaccounting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.albumaccounting.UserTestData.*;
import static ru.javaprojects.albumaccounting.util.exception.ErrorType.BAD_CREDENTIALS_ERROR;
import static ru.javaprojects.albumaccounting.util.exception.ErrorType.UNAUTHORIZED_ERROR;
import static ru.javaprojects.albumaccounting.web.controller.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void changePassword() throws Exception {
        perform((MockMvcRequestBuilders.patch(REST_URL + "/password"))
                .param("password", "newPassword"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void changePasswordInvalid() throws Exception {
        perform((MockMvcRequestBuilders.patch(REST_URL + "/password"))
                .param("password", "new"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void login() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .param("email", user.getEmail())
                .param("password", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void loginFailed() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + "/login")
                .param("email", user.getEmail())
                .param("password", "wrongPassword"))
                .andExpect(status().isBadRequest())
                .andExpect(errorType(BAD_CREDENTIALS_ERROR));
    }
}