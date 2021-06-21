package ru.javaprojects.albumaccounting.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.javaprojects.albumaccounting.model.Role;
import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.to.UserTo;
import ru.javaprojects.albumaccounting.util.UserUtil;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javaprojects.albumaccounting.UserTestData.*;

class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () -> service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ARCHIVE_WORKER)));
    }

    @Test
    void get() {
        User user = service.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail(admin.getEmail());
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getByEmailNotFound() {
        assertThrows(NotFoundException.class, () -> service.getByEmail("notExistedEmail@yandex.ru"));
    }

    @Test
    void getAll() {
        List<User> users = service.getAll();
        USER_MATCHER.assertMatch(users, List.of(admin, user));
    }

    @Test
    void getByNameKeyWord() {
        List<User> users = service.getAllByKeyWord("admin");
        USER_MATCHER.assertMatch(users, List.of(admin));
        users = service.getAllByKeyWord("AdMin");
        USER_MATCHER.assertMatch(users, List.of(admin));
        users = service.getAllByKeyWord("Adm");
        USER_MATCHER.assertMatch(users, List.of(admin));
        users = service.getAllByKeyWord("Min");
        USER_MATCHER.assertMatch(users, List.of(admin));
    }

    @Test
    void getByEmailKeyWord() {
        List<User> users = service.getAllByKeyWord("@yandex.ru");
        USER_MATCHER.assertMatch(users, List.of(user));
        users = service.getAllByKeyWord("@YAndex.ru");
        USER_MATCHER.assertMatch(users, List.of(user));
        users = service.getAllByKeyWord("@");
        USER_MATCHER.assertMatch(users, List.of(admin, user));
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void update() {
        UserTo updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), UserUtil.updateFromTo(new User(user), getUpdated()));
    }

    @Test
    void updateNotFound() {
        UserTo updated = getUpdated();
        updated.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updated));
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.ARCHIVE_WORKER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.ARCHIVE_WORKER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.ARCHIVE_WORKER)));
    }

    @Test
    void enable() {
        service.enable(USER_ID, false);
        assertFalse(service.get(USER_ID).isEnabled());
        service.enable(USER_ID, true);
        assertTrue(service.get(USER_ID).isEnabled());
    }

    @Test
    void enableNotFound() {
        assertThrows(NotFoundException.class, () -> service.enable(NOT_FOUND, false));
    }

    @Test
    void changePassword() {
        User user = service.get(USER_ID);
        service.changePassword(USER_ID, "newPassword");
        assertNotEquals(user.getPassword(), service.get(USER_ID).getPassword());
    }

    @Test
    void changePasswordNotFound() {
        assertThrows(NotFoundException.class, () -> service.changePassword(NOT_FOUND, "newPassword"));
    }
}