package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    User createUser(User newUser);

    User getById(long id) throws IdNotFoundException;

    List<User> getAllUsers() throws SQLException;

    User updateUser(User updatedUser);

    String deleteUser(long id);

    boolean idIsExists(long id);

    boolean emailIsExists(String email);
}
