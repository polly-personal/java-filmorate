package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;
import ru.yandex.practicum.filmorate.storage.dao.impl.UserDaoImpl;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User newUser) throws ValidationException {
        return userDao.createUser(newUser);
    }

    @Override
    public User getById(long id) throws ValidationException, IdNotFoundException {
        return userDao.getById(id);
    }

    @Override
    public List<User> getUsersList() {
        return userDao.getUsersList();
    }

    @Override
    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException {
        return userDao.updateUser(updatedUser);
    }

    @Override
    public String deleteUser(long id) throws IdNotFoundException, ValidationException {
        return userDao.deleteUser(id);
    }

    @Override
    public User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException {
        return userDao.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException, FriendsListNotFoundException {
        return userDao.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(long id) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        Set<Long> friendsIds = userDao.getFriends(id);
        if (friendsIds == null) {
            return null;
        }

        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsIds) {
            friends.add(userDao.getById(friendId));
        }

        return friends;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        userDao.idValidation(id);
        userDao.idValidation(otherId);

        List<User> friendsById = new ArrayList<>(getFriends(id));
        List<User> friendsByOtherId = new ArrayList<>(getFriends(otherId));

        friendsById.retainAll(friendsByOtherId);
        return friendsById;
    }
}
