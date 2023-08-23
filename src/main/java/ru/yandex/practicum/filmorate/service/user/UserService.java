package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User createUser(User newUser) throws ValidationException;

    User getById(long id) throws IdNotFoundException;

    List<User> getAllUsers() throws SQLException;

    User updateUser(User updatedUser) throws IdNotFoundException, ValidationException;

    String deleteUser(long id) throws IdNotFoundException;

    boolean idIsValid(long id) throws IdNotFoundException;

    void userValidation(User user) throws ValidationException;
}
