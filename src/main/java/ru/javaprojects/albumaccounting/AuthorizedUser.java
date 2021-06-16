package ru.javaprojects.albumaccounting;


import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.to.UserTo;
import ru.javaprojects.albumaccounting.util.UserUtil;

import java.io.Serial;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    @Serial
    private static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, user.getRoles());
        userTo = UserUtil.asTo(user);
    }

    public int getId() {
        return userTo.getId();
    }

    public String getUserEmail() {
        return userTo.getEmail();
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}