package ru.javaprojects.albumaccounting.to;

import ru.javaprojects.albumaccounting.HasId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EmployeeTo implements HasId {
    private Integer id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank
    @Size(min = 5, max = 20)
    private String phoneNumber;

    @NotNull
    private Integer departmentId;

    public EmployeeTo() {
    }

    public EmployeeTo(Integer id, String name, String phoneNumber, Integer departmentId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.departmentId = departmentId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "EmployeeTo{" +
                "id=" + id +
                ", name=" + name +
                ", phoneNumber=" + phoneNumber +
                ", departmentId=" + departmentId +
                '}';
    }
}