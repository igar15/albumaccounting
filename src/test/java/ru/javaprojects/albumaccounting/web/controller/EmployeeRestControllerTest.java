package ru.javaprojects.albumaccounting.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.repository.EmployeeRepository;
import ru.javaprojects.albumaccounting.service.EmployeeService;
import ru.javaprojects.albumaccounting.to.EmployeeTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;
import ru.javaprojects.albumaccounting.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaprojects.albumaccounting.DepartmentTestData.*;
import static ru.javaprojects.albumaccounting.EmployeeTestData.NOT_FOUND;
import static ru.javaprojects.albumaccounting.EmployeeTestData.getNew;
import static ru.javaprojects.albumaccounting.EmployeeTestData.getUpdated;
import static ru.javaprojects.albumaccounting.EmployeeTestData.*;
import static ru.javaprojects.albumaccounting.TestUtil.readFromJson;
import static ru.javaprojects.albumaccounting.UserTestData.ADMIN_MAIL;
import static ru.javaprojects.albumaccounting.UserTestData.USER_MAIL;
import static ru.javaprojects.albumaccounting.util.exception.ErrorType.*;
import static ru.javaprojects.albumaccounting.web.AppExceptionHandler.EXCEPTION_DUPLICATE_EMPLOYEE;
import static ru.javaprojects.albumaccounting.web.AppExceptionHandler.EXCEPTION_HOLDER_HAS_ALBUMS;

class EmployeeRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = EmployeeRestController.REST_URL + '/';

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "departments/" + DEPARTMENT_1_ID + "/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(EMPLOYEE_MATCHER.contentJson(employee3, employee1, employee2));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllBadDepartment() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "departments/" + NOT_FOUND + "/employees"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void getAllUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "departments/" + DEPARTMENT_1_ID + "/employees"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "employees/" + EMPLOYEE_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(EMPLOYEE_MATCHER.contentJson(employee1));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "employees/" + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "employees/" + EMPLOYEE_1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "employees/" + EMPLOYEE_3_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> employeeService.get(EMPLOYEE_3_ID));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void deleteWhenEmployeeHasAlbums() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "employees/" + EMPLOYEE_1_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_HOLDER_HAS_ALBUMS));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "employees/" + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void deleteUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "employees/" + EMPLOYEE_1_ID))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createWithLocation() throws Exception {
        EmployeeTo newEmployeeTo = getNewTo();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEmployeeTo)))
                .andExpect(status().isCreated());

        Employee created = readFromJson(action, Employee.class);
        int newId = created.id();
        Employee newEmployee = getNew();
        newEmployee.setId(newId);
        EMPLOYEE_MATCHER.assertMatch(created, newEmployee);
        EMPLOYEE_MATCHER.assertMatch(employeeService.get(newId), newEmployee);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createBadDepartment() throws Exception {
        EmployeeTo newEmployeeTo = getNewTo();
        newEmployeeTo.setDepartmentId(NOT_FOUND);
        perform(MockMvcRequestBuilders.post(REST_URL + "/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEmployeeTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void createUnAuth() throws Exception {
        EmployeeTo newEmployeeTo = getNewTo();
        perform(MockMvcRequestBuilders.post(REST_URL + "/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEmployeeTo)))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        EmployeeTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL + "employees/" + EMPLOYEE_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent());

        EMPLOYEE_MATCHER.assertMatch(employeeService.get(EMPLOYEE_1_ID), getUpdated());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateBadDepartment() throws Exception {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setDepartmentId(NOT_FOUND);
        perform(MockMvcRequestBuilders.put(REST_URL + "employees/" + EMPLOYEE_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateChangeDepartment() throws Exception {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setDepartmentId(DEPARTMENT_2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + "employees/" + EMPLOYEE_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isNoContent());

        Employee employee = employeeRepository.findByIdWithDepartment(EMPLOYEE_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employee, getUpdated());
        DEPARTMENT_MATCHER.assertMatch(employee.getDepartment(), department2);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotFound() throws Exception {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + "employees/" + NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(DATA_NOT_FOUND));
    }

    @Test
    void updateUnAuth() throws Exception {
        EmployeeTo updatedTo = getUpdatedTo();
        perform(MockMvcRequestBuilders.put(REST_URL + "employees/" + EMPLOYEE_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andExpect(status().isUnauthorized())
                .andExpect(errorType(UNAUTHORIZED_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalid() throws Exception {
        EmployeeTo newEmployeeTo = getNewTo();
        newEmployeeTo.setName(" ");
        perform(MockMvcRequestBuilders.post(REST_URL + "/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEmployeeTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setDepartmentId(null);
        perform(MockMvcRequestBuilders.put(REST_URL + "/employees/" + EMPLOYEE_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicateNamePhoneNumber() throws Exception {
        EmployeeTo newEmployeeTo = new EmployeeTo(null, employee1.getName(), employee1.getPhoneNumber(), DEPARTMENT_1_ID);
        perform(MockMvcRequestBuilders.post(REST_URL + "/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newEmployeeTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMPLOYEE));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicateNamePhoneNumber() throws Exception {
        EmployeeTo updatedTo = new EmployeeTo(EMPLOYEE_1_ID, employee2.getName(), employee2.getPhoneNumber(), DEPARTMENT_2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL + "/employees/" + EMPLOYEE_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR))
                .andExpect(detailMessage(EXCEPTION_DUPLICATE_EMPLOYEE));
    }
}