package ru.javaprojects.albumaccounting.util;

import ru.javaprojects.albumaccounting.model.Employee;
import ru.javaprojects.albumaccounting.to.EmployeeTo;

public class EmployeeUtil {

    private EmployeeUtil() {
    }

    public static Employee createFromTo(EmployeeTo employeeTo) {
        return new Employee(employeeTo.getId(), employeeTo.getName(), employeeTo.getPhoneNumber());
    }

    public static Employee updateFromTo(Employee employee, EmployeeTo employeeTo) {
        employee.setName(employeeTo.getName());
        employee.setPhoneNumber(employeeTo.getPhoneNumber());
        return employee;
    }
}