package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.sql.SQLException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User newUser) throws ValidationException {
        userValidation(newUser);
        return userDao.createUser(newUser);
    }

    @Override
    public User getById(long id) throws IdNotFoundException {
        idIsValid(id);
        return userDao.getById(id);
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        return userDao.getAllUsers();
    }

    @Override
    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException {
        idIsValid(updatedUser.getId());
        userValidation(updatedUser);

        return userDao.updateUser(updatedUser);
    }

    @Override
    public String deleteUser(long id) throws IdNotFoundException {
        idIsValid(id);
        return userDao.deleteUser(id);
    }

    @Override
    public boolean idIsValid(long id) throws IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        if (!userDao.idIsExists(id)) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }
        return true;
    }

    @Override
    public void userValidation(User user) throws ValidationException {
        emailValidation(user.getEmail());

        String login = user.getLogin();
        loginValidation(login);

        chooseLoginOrName(user, user.getName(), user.getLogin());
    }

    private boolean emailValidation(String email) throws ValidationException {
        if (!userDao.emailIsExists(email)) {
            throw new ValidationException("пользователь с таким email уже существует");
        }
        return true;
    }

    private void loginValidation(String login) throws ValidationException {
        if (login.contains(" ")) {
            throw new ValidationException("поле \"login\" должно быть без пробелов");
        }
    }

    private boolean nameIsEmpty(String name) {
        if (name == null || name.isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    private void chooseLoginOrName(User user, String name, String login) {
        if (nameIsEmpty(name)) {
            user.setName(login);
        } else {
            user.setName(name);
        }
    }
}
