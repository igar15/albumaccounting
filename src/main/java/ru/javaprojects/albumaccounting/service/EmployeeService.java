package ru.javaprojects.albumaccounting.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaprojects.albumaccounting.model.Department;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.repository.EmployeeRepository;
import ru.javaprojects.albumaccounting.to.EmployeeTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import java.util.List;

import static ru.javaprojects.albumaccounting.util.EmployeeUtil.createFromTo;
import static ru.javaprojects.albumaccounting.util.EmployeeUtil.updateFromTo;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final DepartmentService departmentService;

    public EmployeeService(EmployeeRepository repository, DepartmentService departmentService) {
        this.repository = repository;
        this.departmentService = departmentService;
    }

    @CacheEvict(value = "employees", allEntries = true)
    @Transactional
    public Employee create(EmployeeTo employeeTo) {
        Assert.notNull(employeeTo, "employeeTo must not be null");
        Department department = departmentService.get(employeeTo.getDepartmentId());
        Employee employee = createFromTo(employeeTo);
        employee.setDepartment(department);
        return repository.save(employee);
    }

    public Employee get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found employee with id=" + id));
    }

    @Cacheable(value = "employees", key = "#departmentId")
    public List<Employee> getAllByDepartmentId(int departmentId) {
        departmentService.get(departmentId);
        return repository.findAllByDepartmentIdOrderByName(departmentId);
    }

    @CacheEvict(value = "employees", allEntries = true)
    public void delete(int id) {
        Employee employee = get(id);
        repository.delete(employee);
    }

    @CacheEvict(value = "employees", allEntries = true)
    @Transactional
    public void update(EmployeeTo employeeTo) {
        Assert.notNull(employeeTo, "employeeTo must not be null");
        Employee employee = get(employeeTo.getId());
        Department department = departmentService.get(employeeTo.getDepartmentId());
        updateFromTo(employee, employeeTo);
        employee.setDepartment(department);
    }
}