package ru.javaprojects.albumaccounting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import ru.javaprojects.albumaccounting.model.Album;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaprojects.albumaccounting.AlbumTestData.getNew;
import static ru.javaprojects.albumaccounting.AlbumTestData.getUpdated;
import static ru.javaprojects.albumaccounting.AlbumTestData.*;
import static ru.javaprojects.albumaccounting.EmployeeTestData.NOT_FOUND;
import static ru.javaprojects.albumaccounting.EmployeeTestData.*;
import static ru.javaprojects.albumaccounting.model.Stamp.I_STAMP;

class AlbumServiceTest extends AbstractServiceTest {

    @Autowired
    private AlbumService service;

    @Test
    void create() {
        Album created = service.create(getNew(), EMPLOYEE_1_ID);
        int newId = created.id();
        Album newAlbum = getNew();
        newAlbum.setId(newId);
        ALBUM_MATCHER.assertMatch(created, newAlbum);
        ALBUM_MATCHER.assertMatch(service.get(newId), newAlbum);
    }

    @Test
    void duplicateDecimalNumberStampCreate() {
        assertThrows(DataAccessException.class, () -> service.create(new Album(null, "ВУИА.444444.005", I_STAMP), EMPLOYEE_1_ID));
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
    void getAlbums() {
        Page<Album> albums = service.getAlbums(PAGEABLE);
        assertEquals(PAGE, albums);
        ALBUM_MATCHER.assertMatch(albums.getContent(), List.of(album1, album2, album3, album4));
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
        service.update(getUpdated(), EMPLOYEE_1_ID);
        ALBUM_MATCHER.assertMatch(service.get(ALBUM_1_ID), getUpdated());
    }

    @Test
    void updateNotFound() {
        Album updated = getUpdated();
        updated.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updated, EMPLOYEE_1_ID));
    }

    @Test
    void updateChangeHolder() {
        service.update(getUpdated(), EMPLOYEE_2_ID);
        Album updated = service.get(ALBUM_1_ID);
        ALBUM_MATCHER.assertMatch(updated, getUpdated());
        EMPLOYEE_MATCHER.assertMatch(updated.getHolder(), employee2);
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Album(null, " ", I_STAMP), EMPLOYEE_1_ID));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Album(null, "VUIA.4444", I_STAMP), EMPLOYEE_1_ID));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Album(null, "VUIA.444444.001", null), EMPLOYEE_1_ID));
    }
}