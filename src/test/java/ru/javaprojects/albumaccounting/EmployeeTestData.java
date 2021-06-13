package ru.javaprojects.albumaccounting;

import ru.javaprojects.albumaccounting.model.Employee;

import static ru.javaprojects.albumaccounting.model.AbstractBaseEntity.START_SEQ;

public class EmployeeTestData {
    public static final TestMatcher<Employee> EMPLOYEE_MATCHER = TestMatcher.usingIgnoringFieldsComparator(Employee.class, "department");

    public static final int EMPLOYEE_1_ID = START_SEQ + 5;
    public static final int EMPLOYEE_2_ID = START_SEQ + 6;
    public static final int EMPLOYEE_3_ID = START_SEQ + 7;
    public static final int NOT_FOUND = 10;

    public static final Employee employee1 = new Employee(EMPLOYEE_1_ID, "Naumkin A.", "1-31-65");
    public static final Employee employee2 = new Employee(EMPLOYEE_2_ID, "Yachuk M.", "1-32-65");
    public static final Employee employee3 = new Employee(EMPLOYEE_3_ID, "Nastenko I.", "1-33-65");

    public static Employee getNew() {
        return new Employee(null, "NewName", "NewPhoneNumber");
    }

    public static Employee getUpdated() {
        return new Employee(EMPLOYEE_1_ID, "UpdatedName", "UpdatedPhoneNumber");
    }
}