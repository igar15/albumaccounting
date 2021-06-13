package ru.javaprojects.albumaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaprojects.albumaccounting.model.Department;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    List<Department> findAllByOrderByName();
}
