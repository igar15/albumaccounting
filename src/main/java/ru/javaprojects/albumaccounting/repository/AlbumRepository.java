package ru.javaprojects.albumaccounting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaprojects.albumaccounting.model.Album;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    @EntityGraph(attributePaths = {"holder"})
    Optional<Album> findById(int id);

    @EntityGraph(attributePaths = {"holder", "holder.department"})
    Page<Album> findAllByOrderByDecimalNumberAscStamp(Pageable pageable);

    @EntityGraph(attributePaths = {"holder", "holder.department"})
    Page<Album> findAllByDecimalNumberContainsIgnoreCaseOrderByDecimalNumberAscStamp(String keyWord, Pageable pageable);

    @EntityGraph(attributePaths = {"holder", "holder.department"})
    Page<Album> findAllByHolder_NameContainsIgnoreCaseOrderByDecimalNumberAscStamp(String holderName, Pageable pageable);
}