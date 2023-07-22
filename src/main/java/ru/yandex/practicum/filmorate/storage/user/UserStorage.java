package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User createUser(User newUser) throws ValidationException;

    public User getById(long id) throws ValidationException, IdNotFoundException;

    public List<User> getUsersList();

    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException;

    public String deleteUser(long id) throws IdNotFoundException, ValidationException;
}
