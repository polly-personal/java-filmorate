package ru.yandex.practicum.filmorate.service.friendship;

import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.util.List;

public interface FriendshipService {
    void addFriendOnlyForUser(long userId, long friendId) throws IdNotFoundException;

    List<User> getUserFriends(long userId) throws IdNotFoundException, FriendsListNotFoundException, SQLException;

    void deleteFriendForUserOnly(long userId, long friendId) throws IdNotFoundException;

    List<User> getCommonFriends(long userId, long otherId) throws IdNotFoundException, SQLException, FriendsListNotFoundException;
}
