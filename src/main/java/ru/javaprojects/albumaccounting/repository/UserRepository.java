package ru.javaprojects.albumaccounting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.User;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByNameAscEmail();

    List<User> findAllByNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrderByNameAscEmail(String nameKeyWord, String emailKeyWord);
}