package ru.javaprojects.albumaccounting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.Employee;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByDepartmentIdOrderByName(int departmentId);

    @EntityGraph(attributePaths = {"department"})
    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Employee findByIdWithDepartment(@Param("id") int id);
}