package ru.javaprojects.albumaccounting.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "employees", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "phone_number"}, name = "employees_unique_name_phone_number_idx")})
public class Employee extends AbstractNamedEntity {

    @NotBlank
    @Size(min = 5, max = 20)
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Department department;

    public Employee() {
    }

    public Employee(Integer id, String name, String phoneNumber) {
        super(id, name);
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name=" + name +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
