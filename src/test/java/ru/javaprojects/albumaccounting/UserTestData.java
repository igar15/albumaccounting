package ru.javaprojects.albumaccounting;

import ru.javaprojects.albumaccounting.model.Role;
import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.to.UserTo;
import ru.javaprojects.albumaccounting.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static ru.javaprojects.albumaccounting.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final TestMatcher<User> USER_MATCHER = TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final String USER_MAIL = "user@yandex.ru";
    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ARCHIVE_WORKER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ADMIN, Role.ARCHIVE_WORKER);

    public static User getNew() {
        return new User(null, "NewName", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.ARCHIVE_WORKER));
    }

    public static UserTo getUpdated() {
        return new UserTo(USER_ID, "UpdatedName", "update@gmail.com", false, Set.of(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String password) {
        return JsonUtil.writeAdditionProps(user, "password", password);
    }
}