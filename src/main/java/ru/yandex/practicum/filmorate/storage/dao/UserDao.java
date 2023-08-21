package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {
    User createUser(User newUser) throws ValidationException;

    User getById(long id) throws ValidationException, IdNotFoundException;

    List<User> getUsersList();

    User updateUser(User updatedUser) throws IdNotFoundException, ValidationException;

    String deleteUser(long id) throws IdNotFoundException, ValidationException;

    User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException;

    User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException, FriendsListNotFoundException;

    Set<Long> getFriends(long id) throws ValidationException, IdNotFoundException, FriendsListNotFoundException;

    void idValidation(long id) throws ValidationException, IdNotFoundException;

    void userValidation(User user);
}
