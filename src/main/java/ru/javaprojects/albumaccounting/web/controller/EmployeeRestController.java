package ru.javaprojects.albumaccounting.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.service.EmployeeService;
import ru.javaprojects.albumaccounting.to.EmployeeTo;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaprojects.albumaccounting.util.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.albumaccounting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = EmployeeRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Secured({"ROLE_ARCHIVE_WORKER", "ROLE_ADMIN"})
@Tag(name = "Employee Controller")
public class EmployeeRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api";

    @Autowired
    private EmployeeService service;

    @Operation(description = "Get all employees for department")
    @GetMapping("/departments/{departmentId}/employees")
    public List<Employee> getAll(@PathVariable int departmentId) {
        log.info("getAll for department {}", departmentId);
        return service.getAllByDepartmentId(departmentId);
    }

    @Operation(description = "Get employee")
    @GetMapping("/employees/{id}")
    public Employee get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @Operation(description = "Delete employee")
    @DeleteMapping("/employees/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @Operation(description = "Create new employee")
    @PostMapping(value = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> createWithLocation(@Valid @RequestBody EmployeeTo employeeTo) {
        log.info("create {}", employeeTo);
        checkNew(employeeTo);
        Employee created = service.create(employeeTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/employees/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(description = "Update employee")
    @PutMapping(value = "/employees/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody EmployeeTo employeeTo, @PathVariable int id) {
        log.info("update {} with id={}", employeeTo, id);
        assureIdConsistent(employeeTo, id);
        service.update(employeeTo);
    }
}