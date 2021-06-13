package ru.javaprojects.albumaccounting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javaprojects.albumaccounting.UserTestData;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaprojects.albumaccounting.UserTestData.*;
import static ru.javaprojects.albumaccounting.web.controller.ProfileRestController.*;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void changePassword() throws Exception {
        perform((MockMvcRequestBuilders.patch(REST_URL + "/password"))
                .param("password", "newPassword"))
                .andExpect(status().isNoContent());
    }
}