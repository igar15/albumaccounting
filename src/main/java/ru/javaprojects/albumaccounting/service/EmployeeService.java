package ru.javaprojects.albumaccounting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaprojects.albumaccounting.model.Department;
import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.repository.EmployeeRepository;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final DepartmentService departmentService;

    public EmployeeService(EmployeeRepository repository, DepartmentService departmentService) {
        this.repository = repository;
        this.departmentService = departmentService;
    }

    @Transactional
    public Employee create(Employee employee, int departmentId) {
        Assert.notNull(employee, "employee must not be null");
        Department department = departmentService.get(departmentId);
        employee.setDepartment(department);
        return repository.save(employee);
    }

    public Employee get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found employee with id=" + id));
    }

    public List<Employee> getAllByDepartmentId(int departmentId) {
        departmentService.get(departmentId);
        return repository.findAllByDepartmentIdOrderByName(departmentId);
    }

    public void delete(int id) {
        Employee employee = get(id);
        repository.delete(employee);
    }

    @Transactional
    public void update(Employee employee, int departmentId) {
        Assert.notNull(employee, "employee must not be null");
        get(employee.id());
        Department department = departmentService.get(departmentId);
        employee.setDepartment(department);
        repository.save(employee);
    }
}