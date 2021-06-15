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
import ru.javaprojects.albumaccounting.model.Department;
import ru.javaprojects.albumaccounting.service.DepartmentService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaprojects.albumaccounting.util.ValidationUtil.assureIdConsistent;
import static ru.javaprojects.albumaccounting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = DepartmentRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Department Controller")
public class DepartmentRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/api/departments";

    @Autowired
    private DepartmentService service;

    @Operation(description = "Get all departments")
    @Secured({"ROLE_ARCHIVE_WORKER", "ROLE_ADMIN"})
    @GetMapping
    public List<Department> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    @Operation(description = "Get department")
    @Secured({"ROLE_ARCHIVE_WORKER", "ROLE_ADMIN"})
    @GetMapping("/{id}")
    public Department get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @Operation(description = "Delete department (Admin only)")
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    @Operation(description = "Create new department (Admin only)")
    @Secured("ROLE_ADMIN")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> createWithLocation(@Valid @RequestBody Department department) {
        log.info("create {}", department);
        checkNew(department);
        Department created = service.create(department);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(description = "Update department (Admin only)")
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Department department, @PathVariable int id) {
        log.info("update {} with id={}", department, id);
        assureIdConsistent(department, id);
        service.update(department);
    }
}