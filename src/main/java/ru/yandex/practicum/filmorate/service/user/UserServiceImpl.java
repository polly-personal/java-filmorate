package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User newUser) throws ValidationException {
        userValidation(newUser);
        return userDao.createUser(newUser);
    }

    @Override
    public User getById(long id) throws ValidationException, IdNotFoundException {
        idValidation(id);
        return userDao.getById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException {
        userDao.idValidation(updatedUser.getId());
        userValidation(updatedUser);

        return userDao.updateUser(updatedUser);
    }

    @Override
    public String deleteUser(long id) throws IdNotFoundException, ValidationException {
        idValidation(id);
        return userDao.deleteUser(id);
    }

    @Override
    public User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException {
        idValidation(id);
        idValidation(friendId);
        return userDao.addFriend(id, friendId);
    }

    @Override
    public User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException, FriendsListNotFoundException {
        idValidation(id);
        idValidation(friendId);
        return userDao.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(long id) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        idValidation(id);

        Set<Long> friendsIds = userDao.getFriends(id);
        if (friendsIds == null) {
            throw new FriendsListNotFoundException("список друзей пуст");
        }

        List<User> friends = new ArrayList<>();
        for (Long friendId : friendsIds) {
            friends.add(userDao.getById(friendId));
        }

        return friends;
    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        idValidation(id);
        idValidation(otherId);

        List<User> friendsById = new ArrayList<>(getFriends(id));
        List<User> friendsByOtherId = new ArrayList<>(getFriends(otherId));

        friendsById.retainAll(friendsByOtherId);
        return friendsById;
    }

    @Override
    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }
    }

    public void userValidation(User user) {
        emailValidation(user.getEmail());

        String login = user.getLogin();
        loginValidation(login);

        chooseLoginOrName(user, user.getName(), user.getLogin());
    }

    private boolean emailValidation(String email) throws ValidationException {
        if (userDao.emailIsValid(email)) {
            return true;
        }
        throw new ValidationException("пользователь с таким email уже существует");
    }

    private void loginValidation(String login) throws ValidationException {
        if (login.contains(" ")) {
            throw new ValidationException("поле \"login\" должно быть без пробелов");
        }
    }

    private boolean nameIsEmpty(String name) {
        if (name == null || name.isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    private void chooseLoginOrName(User user, String name, String login) {
        if (nameIsEmpty(name)) {
            user.setName(login);
        } else {
            user.setName(name);
        }
    }
}
