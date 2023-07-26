package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserServiceImpl(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User createUser(User newUser) throws ValidationException {
        return inMemoryUserStorage.createUser(newUser);
    }

    public User getById(long id) throws ValidationException, IdNotFoundException {
        return inMemoryUserStorage.getById(id);
    }

    public List<User> getUsersList() {
        return inMemoryUserStorage.getUsersList();
    }

    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException {
        return inMemoryUserStorage.updateUser(updatedUser);
    }

    public String deleteUser(long id) throws IdNotFoundException, ValidationException {
        return inMemoryUserStorage.deleteUser(id);
    }

    public User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException {
        inMemoryUserStorage.idValidation(id);
        inMemoryUserStorage.idValidation(friendId);
        User user = getById(id);
        Set<Long> userFriendsIds = user.getFriendIds();
        if (friendsListIsEmpty(userFriendsIds)) {
            userFriendsIds = new HashSet<>();
        }
        userFriendsIds.add(friendId);
        user.setFriendIds(userFriendsIds);

        User friend = getById(friendId);
        Set<Long> friendFriendsIds = friend.getFriendIds();
        if (friendsListIsEmpty(friendFriendsIds)) {
            friendFriendsIds = new HashSet<>();
        }
        friendFriendsIds.add(id);
        friend.setFriendIds(friendFriendsIds);

        return user;
    }

    public User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException, FriendsListNotFoundException {
        inMemoryUserStorage.idValidation(id);
        inMemoryUserStorage.idValidation(friendId);

        User user = getById(id);
        Set<Long> userFriendsIds = user.getFriendIds();
        friendsListValidation(userFriendsIds, friendId);
        userFriendsIds.remove(friendId);

        User friend = getById(friendId);
        Set<Long> friendFriendsIds = friend.getFriendIds();
        friendsListValidation(friendFriendsIds, id);
        friendFriendsIds.remove(id);

        return user;
    }

    public List<User> getFriends(long id) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        inMemoryUserStorage.idValidation(id);
        User user = getById(id);

        Set<Long> friendsIds = user.getFriendIds();
        friendsListValidation(friendsIds);

        List<User> friendsList = new ArrayList<>();
        for (Long friendId : friendsIds) {
            User friend = getById(friendId);
            friendsList.add(friend);
        }

        return friendsList;
    }

    public List<User> getCommonFriends(long id, long otherId) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        inMemoryUserStorage.idValidation(id);
        inMemoryUserStorage.idValidation(otherId);

        User user = getById(id);
        Set<Long> userFriendIds = user.getFriendIds();

        User otherUser = getById(otherId);
        Set<Long> otherUserFriendsIds = otherUser.getFriendIds();

        List<User> commonFriendsList = new ArrayList<>();
        if (userFriendIds != null && otherUserFriendsIds != null) {
            commonFriendsList = userFriendIds.stream()
                    .filter(otherUserFriendsIds::contains)
                    .map(inMemoryUserStorage::getById)
                    .collect(Collectors.toList());
        }
        return commonFriendsList;
    }

    public void likedFilmsListValidation(Set<Long> likes, long filmId) throws ValidationException {
        if (likes == null) {
            throw new FriendsListNotFoundException("у пользователя нет лайков");
        }
        if (!likes.contains(filmId)) {
            throw new ValidationException("у пользователя нет лайка для фильма с filmId: " + filmId);
        }
    }

    private boolean friendsListIsEmpty(Set<Long> friends) {
        return friends == null;
    }

    private void friendsListValidation(Set<Long> friends) throws FriendsListNotFoundException {
        if (friendsListIsEmpty(friends)) {
            throw new FriendsListNotFoundException("у пользователя нет друзей");
        }
    }

    private void friendsListValidation(Set<Long> friends, long friendId) throws ValidationException {
        if (friendsListIsEmpty(friends)) {
            throw new FriendsListNotFoundException("у пользователя нет друзей");
        }
        if (!friends.contains(friendId)) {
            throw new ValidationException("у пользователя нет друга с friendId: " + friendId);
        }
    }
}
