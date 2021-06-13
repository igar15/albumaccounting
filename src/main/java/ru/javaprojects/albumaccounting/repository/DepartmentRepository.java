package ru.javaprojects.albumaccounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.Department;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    List<Department> findAllByOrderByName();
}