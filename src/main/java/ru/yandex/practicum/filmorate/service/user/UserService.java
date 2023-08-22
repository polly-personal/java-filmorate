package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User newUser) throws ValidationException;

    User getById(long id) throws ValidationException, IdNotFoundException;

    List<User> getAllUsers();

    User updateUser(User updatedUser) throws IdNotFoundException, ValidationException;

    String deleteUser(long id) throws IdNotFoundException, ValidationException;

    User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException;

    User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException, FriendsListNotFoundException;

    List<User> getFriends(long id) throws ValidationException, IdNotFoundException, FriendsListNotFoundException;

    List<User> getCommonFriends(long id, long otherId) throws ValidationException, IdNotFoundException, FriendsListNotFoundException;

    void idValidation(long id);
}
