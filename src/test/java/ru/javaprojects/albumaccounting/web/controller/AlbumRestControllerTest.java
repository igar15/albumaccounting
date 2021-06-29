package ru.javaprojects.albumaccounting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.service.AlbumService;
import ru.javaprojects.albumaccounting.to.AlbumTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;
import ru.javaprojects.albumaccounting.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.albumaccounting.AlbumTestData.getNew;
import static ru.javaprojects.albumaccounting.AlbumTestData.getNewTo;
import static ru.javaprojects.albumaccounting.AlbumTestData.getUpdated;
import static ru.javaprojects.albumaccounting.AlbumTestData.getUpdatedTo;
import static ru.javaprojects.albumaccounting.AlbumTestData.*;
import static ru.javaprojects.albumaccounting.EmployeeTestData.NOT_FOUND;
import static ru.javaprojects.albumaccounting.EmployeeTestData.*;
import static ru.javaprojects.albumaccounting.TestUtil.readFromJson;
import static ru.javaprojects.albumaccounting.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.albumaccounting.UserTestData.USER_MAIL;
import static ru.javaprojects.albumaccounting.util.exception.ErrorType.*;
import static ru.javaprojects.albumaccounting.web.AppExceptionHandler.EXCEPTION_DUPLICATE_ALBUM;

class AlbumRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AlbumRestController.REST_URL + '/';

    @Autowired
    private AlbumService albumService;

    @Test
    @WithUserDetails(USER_MAIL)
    void getAll() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL)
                .param("page", "0")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Album> albums = JsonUtil.readContentFromPage(action.andReturn().getResponse().getContentAsString(), Album.class);
        ALBUM_MATCHER.assertMatch(albums, album1, album2, album3);
    }

    @Test
    void getAllUnAuthorized() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL)
                .param("page", "0")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Album> albums = JsonUtil.readContentFromPage(action.andReturn().getResponse().getContentAsString(), Album.class);
        ALBUM_MATCHER.assertMatch(albums, album1, album2, album3);
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getAllByDecimalNumber() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "byDecimal")
                .param("decimalNumber", "абвг")
                .param("page", "0")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Album> albums = JsonUtil.readContentFromPage(action.andReturn().getResponse().getContentAsString(), Album.class);
        ALBUM_MATCHER.assertMatch(albums, album1, album2, album3);
    }

    @Test
    void getAllByDecimalNumberUnAuthorized() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "byDecimal")
                .param("decimalNumber", "аб")
                .param("page", "0")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Album> albums = JsonUtil.readContentFromPage(action.andReturn().getResponse().getContentAsString(), Album.class);
        ALBUM_MATCHER.assertMatch(albums, album1, album2);
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void getAllByHolderName() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "byHolder")
                .param("holderName", "петров")
                .param("page", "0")
                .param("size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Album> albums = JsonUtil.readContentFromPage(action.andReturn().getResponse().getContentAsString(), Album.class);
        ALBUM_MATCHER.assertMatch(albums, album1, album2, album3);
    }

    @Test
    void getAllByHolderNameUnAuthorized() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "byHolder")
                .param("holderName", "сидор")
                .param("page", "0")
                .param("size", "2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<Album> albums = JsonUtil.readContentFromPage(action.andReturn().getResponse().getContentAsString(), Album.class);
        ALBUM_MATCHER.assertMatch(albums, album4);
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ALBUM_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ALBUM_MATCHER.contentJson(album1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void getUnAuthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ALBUM_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(ALBUM_MATCHER.contentJson(album1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + ALBUM_1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> albumService.get(ALBUM_1_ID));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + ALBUM_1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception {
        AlbumTo newTo = getNewTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isCreated());

        Album created = readFromJson(action, Album.class);
        int newId = created.id();
        Album newAlbum = getNew();
        newAlbum.setId(newId);
        ALBUM_MATCHER.assertMatch(created, newAlbum);
        ALBUM_MATCHER.assertMatch(albumService.get(newId), newAlbum);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createBadHolder() throws Exception {
        AlbumTo newTo = getNewTo();
        newTo.setHolderId(NOT_FOUND);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void createUnAuth() throws Exception {
        AlbumTo newTo = getNewTo();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        AlbumTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL + ALBUM_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent());

        ALBUM_MATCHER.assertMatch(albumService.get(ALBUM_1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateBadHolder() throws Exception {
        AlbumTo updatedTo = getUpdatedTo();
        updatedTo.setHolderId(NOT_FOUND);
        perform(MockMvcRequestBuilders.put(REST_URL + ALBUM_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateChangeHolder() throws Exception {
        AlbumTo updatedTo = getUpdatedTo();
        updatedTo.setHolderId(EMPLOYEE_2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + ALBUM_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent());

        Album album = albumService.get(ALBUM_1_ID);
        ALBUM_MATCHER.assertMatch(album, getUpdated());
        EMPLOYEE_MATCHER.assertMatch(album.getHolder(), employee2);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        AlbumTo updatedTo = getUpdatedTo();
        updatedTo.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void updateUnAuth() throws Exception {
        AlbumTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL + ALBUM_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    void createInvalid() throws Exception {
        AlbumTo newAlbumTo = getNewTo();
        newAlbumTo.setDecimalNumber(" ");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newAlbumTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @WithUserDetails(USER_MAIL)
    void updateInvalid() throws Exception {
        AlbumTo updatedTo = getUpdatedTo();
        updatedTo.setHolderId(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicateDecimalNumberStamp() throws Exception {
        AlbumTo newAlbumTo = new AlbumTo(null, album1.getDecimalNumber(), album1.getStamp(), LocalDate.now(), EMPLOYEE_1_ID);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newAlbumTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_ALBUM));
    }

    @Test
    @WithUserDetails(ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicateDecimalNumberStamp() throws Exception {
        AlbumTo newAlbumTo = new AlbumTo(ALBUM_1_ID, album2.getDecimalNumber(), album2.getStamp(), LocalDate.now(), EMPLOYEE_1_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + ALBUM_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newAlbumTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_ALBUM));
    }
}