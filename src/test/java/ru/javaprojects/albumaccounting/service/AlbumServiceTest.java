package ru.javaprojects.albumaccounting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.to.AlbumTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaprojects.albumaccounting.AlbumTestData.getNew;
import static ru.javaprojects.albumaccounting.AlbumTestData.getNewTo;
import static ru.javaprojects.albumaccounting.AlbumTestData.getUpdated;
import static ru.javaprojects.albumaccounting.AlbumTestData.getUpdatedTo;
import static ru.javaprojects.albumaccounting.AlbumTestData.*;
import static ru.javaprojects.albumaccounting.DepartmentTestData.DEPARTMENT_MATCHER;
import static ru.javaprojects.albumaccounting.DepartmentTestData.department1;
import static ru.javaprojects.albumaccounting.EmployeeTestData.NOT_FOUND;
import static ru.javaprojects.albumaccounting.EmployeeTestData.*;
import static ru.javaprojects.albumaccounting.model.Stamp.I_STAMP;

class AlbumServiceTest extends AbstractServiceTest {

    @Autowired
    private AlbumService service;

    @Test
    void create() {
        Album created = service.create(getNewTo());
        int newId = created.id();
        Album newAlbum = getNew();
        newAlbum.setId(newId);
        ALBUM_MATCHER.assertMatch(created, newAlbum);
        ALBUM_MATCHER.assertMatch(service.get(newId), newAlbum);
    }

    @Test
    void duplicateDecimalNumberStampCreate() {
        assertThrows(DataAccessException.class, () -> service.create(new AlbumTo(null, album1.getDecimalNumber(), album1.getStamp(), EMPLOYEE_1_ID)));
    }

    @Test
    void get() {
        Album album = service.get(ALBUM_1_ID);
        ALBUM_MATCHER.assertMatch(album, album1);
        EMPLOYEE_MATCHER.assertMatch(album.getHolder(), employee1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        Page<Album> albums = service.getAll(PAGEABLE);
        assertEquals(PAGE, albums);
        ALBUM_MATCHER.assertMatch(albums.getContent(), List.of(album1, album2, album3, album4));
        Album firstAlbum = albums.getContent().get(0);
        EMPLOYEE_MATCHER.assertMatch(firstAlbum.getHolder(), employee1);
        DEPARTMENT_MATCHER.assertMatch(firstAlbum.getHolder().getDepartment(), department1);
    }

    @Test
    void getAllByDecimalNumber() {
        Page<Album> albums = service.getAllByDecimalNumber(album1.getDecimalNumber(), PAGEABLE);
        assertEquals(PAGE_BY_DECIMAL_NUMBER_ONE_TOTAL, albums);
        ALBUM_MATCHER.assertMatch(albums.getContent(), List.of(album1));
        Album firstAlbum = albums.getContent().get(0);
        EMPLOYEE_MATCHER.assertMatch(firstAlbum.getHolder(), employee1);
        DEPARTMENT_MATCHER.assertMatch(firstAlbum.getHolder().getDepartment(), department1);

        albums = service.getAllByDecimalNumber("АБВГ", PAGEABLE);
        assertEquals(PAGE_BY_DECIMAL_NUMBER_FOUR_TOTAL, albums);
        ALBUM_MATCHER.assertMatch(albums.getContent(), List.of(album1, album2, album3, album4));
    }

    @Test
    void getAllByHolderName() {
        Page<Album> albums = service.getAllByHolderName(employee2.getName(), PAGEABLE);
        assertEquals(PAGE_BY_HOLDER_NAME_ONE_TOTAL, albums);
        ALBUM_MATCHER.assertMatch(albums.getContent(), List.of(album4));
        Album firstAlbum = albums.getContent().get(0);
        EMPLOYEE_MATCHER.assertMatch(firstAlbum.getHolder(), employee2);
        DEPARTMENT_MATCHER.assertMatch(firstAlbum.getHolder().getDepartment(), department1);

        albums = service.getAllByHolderName("Петров", PAGEABLE);
        assertEquals(PAGE_BY_HOLDER_NAME_THREE_TOTAL, albums);
        ALBUM_MATCHER.assertMatch(albums.getContent(), List.of(album1, album2, album3));
    }

    @Test
    void delete() {
        service.delete(ALBUM_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(ALBUM_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void update() {
        service.update(getUpdatedTo());
        ALBUM_MATCHER.assertMatch(service.get(ALBUM_1_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        AlbumTo updatedTo = getUpdatedTo();
        updatedTo.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updatedTo));
    }

    @Test
    void updateChangeHolder() {
        AlbumTo updatedTo = getUpdatedTo();
        updatedTo.setHolderId(EMPLOYEE_2_ID);
        service.update(updatedTo);
        Album updated = service.get(ALBUM_1_ID);
        ALBUM_MATCHER.assertMatch(updated, getUpdated());
        EMPLOYEE_MATCHER.assertMatch(updated.getHolder(), employee2);
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new AlbumTo(null, " ", I_STAMP, EMPLOYEE_1_ID)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new AlbumTo(null, "АБВГ.4444", I_STAMP, EMPLOYEE_1_ID)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new AlbumTo(null, "АБВГ.444444.001", null, EMPLOYEE_1_ID)));
    }
}