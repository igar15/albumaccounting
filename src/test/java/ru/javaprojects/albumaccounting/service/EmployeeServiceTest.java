package ru.javaprojects.albumaccounting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.repository.EmployeeRepository;
import ru.javaprojects.albumaccounting.to.EmployeeTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaprojects.albumaccounting.DepartmentTestData.*;
import static ru.javaprojects.albumaccounting.EmployeeTestData.NOT_FOUND;
import static ru.javaprojects.albumaccounting.EmployeeTestData.getNew;
import static ru.javaprojects.albumaccounting.EmployeeTestData.getUpdated;
import static ru.javaprojects.albumaccounting.EmployeeTestData.*;

class EmployeeServiceTest extends AbstractServiceTest {

    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository repository;

    @Test
    void create() {
        Employee created = service.create(getNewTo());
        int newId = created.id();
        Employee newEmployee = getNew();
        newEmployee.setId(newId);
        EMPLOYEE_MATCHER.assertMatch(created, newEmployee);
        EMPLOYEE_MATCHER.assertMatch(service.get(newId), newEmployee);
    }

    @Test
    void createBadDepartment() {
        EmployeeTo newEmployeeTo = getNewTo();
        newEmployeeTo.setDepartmentId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.create(newEmployeeTo));
    }

    @Test
    void duplicateNamePhoneNumberCreate() {
        assertThrows(DataAccessException.class, () -> service.create(new EmployeeTo(null, "Naumkin A.", "1-31-65", DEPARTMENT_1_ID)));
    }

    @Test
    void get() {
        Employee employee = service.get(EMPLOYEE_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employee, employee1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAllByDepartmentId() {
        List<Employee> employees = service.getAllByDepartmentId(DEPARTMENT_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employees, List.of(employee3, employee1, employee2));
    }

    @Test
    void getAllByDepartmentIdBadDepartment() {
        assertThrows(NotFoundException.class, () -> service.getAllByDepartmentId(NOT_FOUND));
    }

    @Test
    void delete() {
        service.delete(EMPLOYEE_3_ID);
        assertThrows(NotFoundException.class, () -> service.get(EMPLOYEE_3_ID));
    }

    @Test
    void deleteWhenEmployeeHasAlbums() {
        assertThrows(DataAccessException.class, () -> service.delete(EMPLOYEE_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void update() {
        service.update(getUpdatedTo());
        EMPLOYEE_MATCHER.assertMatch(service.get(EMPLOYEE_1_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updatedTo));
    }

    @Test
    void updateBadDepartment() {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setDepartmentId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updatedTo));
    }

    @Test
    void updateChangeDepartment() {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setDepartmentId(DEPARTMENT_2_ID);
        service.update(updatedTo);
        Employee employee = repository.findByIdWithDepartment(EMPLOYEE_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employee, getUpdated());
        DEPARTMENT_MATCHER.assertMatch(employee.getDepartment(), department2);
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new EmployeeTo(null, " ", "1-77-77", DEPARTMENT_1_ID)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new EmployeeTo(null, "newName", " ", DEPARTMENT_1_ID)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new EmployeeTo(null, "newName", "7777", DEPARTMENT_1_ID)));
    }
}