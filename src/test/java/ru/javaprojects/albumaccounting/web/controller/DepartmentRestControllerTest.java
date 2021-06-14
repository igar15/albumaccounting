package ru.javaprojects.albumaccounting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.Department;
import ru.javaprojects.albumaccounting.service.DepartmentService;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;
import ru.javaprojects.albumaccounting.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.albumaccounting.DepartmentTestData.*;
import static ru.javaprojects.albumaccounting.TestUtil.readFromJson;
import static ru.javaprojects.albumaccounting.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.albumaccounting.UserTestData.USER_MAIL;
import static ru.javaprojects.albumaccounting.util.exception.ErrorType.*;
import static ru.javaprojects.albumaccounting.web.AppExceptionHandler.EXCEPTION_DUPLICATE_DEPARTMENT;
import static ru.javaprojects.albumaccounting.web.AppExceptionHandler.EXCEPTION_HOLDER_HAS_ALBUMS;

class DepartmentRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = DepartmentRestController.REST_URL + '/';

    @Autowired
    private DepartmentService departmentService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAllWhenAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DEPARTMENT_MATCHER.contentJson(department1, department3, department2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllWhenArchiveWorker() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DEPARTMENT_MATCHER.contentJson(department1, department3, department2));
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getWhenAdmin() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DEPARTMENT_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DEPARTMENT_MATCHER.contentJson(department1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getWhenArchiveWorker() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DEPARTMENT_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DEPARTMENT_MATCHER.contentJson(department1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DEPARTMENT_1_ID))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DEPARTMENT_2_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> departmentService.get(DEPARTMENT_2_ID));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void deleteWhenEmployeeHasAlbums() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DEPARTMENT_1_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_HOLDER_HAS_ALBUMS));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DEPARTMENT_1_ID))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DEPARTMENT_1_ID))
                .andExpect(status().isForbidden())
                .andExpect(errorType(ACCESS_DENIED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Department newDepartment = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDepartment)))
                .andExpect(status().isCreated());

        Department created = readFromJson(action, Department.class);
        int newId = created.id();
        newDepartment.setId(newId);
        DEPARTMENT_MATCHER.assertMatch(created, newDepartment);
        DEPARTMENT_MATCHER.assertMatch(departmentService.get(newId), newDepartment);
    }

    @Test
    void createUnAuth() throws Exception {
        Department newDepartment = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDepartment)))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createForbidden() throws Exception {
        Department newDepartment = getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDepartment)))
                .andExpect(status().isForbidden())
                .andExpect(errorType(ACCESS_DENIED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Department updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DEPARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DEPARTMENT_MATCHER.assertMatch(departmentService.get(DEPARTMENT_1_ID), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        Department updated = getUpdated();
        updated.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void updateUnAuth() throws Exception {
        Department updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DEPARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateForbidden() throws Exception {
        Department updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DEPARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isForbidden())
                .andExpect(errorType(ACCESS_DENIED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        Department newDepartment = getNew();
        newDepartment.setName(" ");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDepartment)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        Department updated = getUpdated();
        updated.setName(" ");
        perform(MockMvcRequestBuilders.put(REST_URL + DEPARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicateName() throws Exception {
        Department newDepartment = new Department(null, department1.getName());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDepartment)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_DEPARTMENT));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicateName() throws Exception {
        Department updated = new Department(DEPARTMENT_1_ID, department2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL + DEPARTMENT_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_DEPARTMENT));
    }
}