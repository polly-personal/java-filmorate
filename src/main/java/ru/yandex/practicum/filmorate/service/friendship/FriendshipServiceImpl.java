package ru.yandex.practicum.filmorate.service.friendship;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class FriendshipServiceImpl implements FriendshipService {
    private final FriendshipDao friendshipDao;
    private final UserService userService;

    public FriendshipServiceImpl(FriendshipDao friendshipDao, UserService userService) {
        this.friendshipDao = friendshipDao;
        this.userService = userService;
    }

    @Override
    public void addFriendOnlyForUser(long userId, long friendId) throws IdNotFoundException {
        userService.idIsValid(userId);
        userService.idIsValid(friendId);

        friendshipDao.sendFriendRequest(userId, friendId);
        friendshipDao.approveFriendRequestForOneUserOnly(userId, friendId);
    }

    @Override
    public List<User> getUserFriends(long userId) throws IdNotFoundException, FriendsListNotFoundException,
            SQLException {
        userService.idIsValid(userId);

        Set<Long> friendsIds = friendshipDao.getFriendsIdsByUserId(userId);
        if (friendsIds == null) {
            throw new FriendsListNotFoundException("список друзей пуст");
        }

        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsIds) {
            friends.add(userService.getById(friendId));
        }

        return friends;
    }

    @Override
    public void deleteFriendForUserOnly(long userId, long friendId) throws IdNotFoundException {
        userService.idIsValid(userId);
        userService.idIsValid(friendId);

        friendshipDao.deleteFriendForUserOnly(userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) throws IdNotFoundException, SQLException, FriendsListNotFoundException {
        userService.idIsValid(userId);
        userService.idIsValid(otherId);

        List<User> friendsById = new ArrayList<>(getUserFriends(userId));
        List<User> friendsByOtherId = new ArrayList<>(getUserFriends(otherId));

        friendsById.retainAll(friendsByOtherId);
        return friendsById;
    }
}
