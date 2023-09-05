package ru.yandex.practicum.filmorate.storage.dao;

import java.sql.SQLException;
import java.util.Set;

public interface FriendshipDao {
    void sendFriendRequest(long userId, long friendId);

    void approveFriendRequestForOneUserOnly(long userId, long friendId);

    void approveFriendRequestForBothUsers(long userId, long friendId);

    Set<Long> getFriendsIdsByUserId(long userId) throws SQLException;

    void deleteFriendForUserOnly(long userId, long friendId);
}
