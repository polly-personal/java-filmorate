package ru.yandex.practicum.filmorate.storage.dao;

import java.util.Set;

public interface FriendshipDao {
    Set<Long> getFriendsIdsByUserId(long id);

    void sendFriendRequest(long userId, long friendId);

    void approveFriendRequestForOneUserOnly(long userId, long friendId);

    void approveFriendRequestForBothUsers(long userId, long friendId);

    void deleteFriend(long userId, long friendId);
}
