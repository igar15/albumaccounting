package ru.javaprojects.albumaccounting.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javaprojects.albumaccounting.model.User;
import ru.javaprojects.albumaccounting.repository.UserRepository;
import ru.javaprojects.albumaccounting.to.UserTo;
import ru.javaprojects.albumaccounting.util.exception.NotFoundException;

import java.util.List;

import static ru.javaprojects.albumaccounting.util.UserUtil.prepareToSave;
import static ru.javaprojects.albumaccounting.util.UserUtil.updateFromTo;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    public User get(int id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not found user with id=" + id));
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NotFoundException("Not found user with email=" + email));
    }

    public List<User> getAll() {
        return repository.findAllByOrderByNameAscEmail();
    }

    public List<User> getAllByKeyWord(String keyWord) {
        return repository.findAllByNameContainsIgnoreCaseOrEmailContainsIgnoreCaseOrderByNameAscEmail(keyWord, keyWord);
    }

    public void delete(int id) {
        User user = get(id);
        repository.delete(user);
    }

    @Transactional
    public void update(UserTo userTo) {
        Assert.notNull(userTo, "userTo must not be null");
        User user = get(userTo.id());
        updateFromTo(user, userTo);
    }

    @Transactional
    public void enable(int id, boolean enabled) {
        User user = get(id);
        user.setEnabled(enabled);
    }

    @Transactional
    public void changePassword(int id, String password) {
        Assert.notNull(password, "password must not be null");
        User user = get(id);
        user.setPassword(passwordEncoder.encode(password));
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }
}