package ru.yandex.practicum.filmorate.service.user;

import lombok.Data;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Data
@Service
public class UserService implements UserStorage {
    private long currentID;
    private Map<Long, User> users = new HashMap();

    public User createUser(User newUser) throws ValidationException {
        emailValidation(newUser.getEmail());
        birthdayValidation(newUser.getBirthday());

        String userLogin = newUser.getLogin();
        loginValidation(userLogin);
        chooseLoginOrName(newUser, newUser.getName(), userLogin);

        newUser.setId(createID());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User getById(long id) throws ValidationException, IdNotFoundException {
        idValidation(id);
        return users.get(id);
    }

    public List<User> getUsersList() {
        List<User> list = new ArrayList<>(users.values());
        return list;
    }

    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException {
        long id = updatedUser.getId();
        idValidation(id);

        emailValidation(updatedUser.getEmail());
        birthdayValidation(updatedUser.getBirthday());

        String updatedLogin = updatedUser.getLogin();
        loginValidation(updatedLogin);
        chooseLoginOrName(updatedUser, updatedUser.getName(), updatedLogin);

        users.put(id, updatedUser);
        return updatedUser;
    }

    public String deleteUser(long id) throws IdNotFoundException, ValidationException {
        idValidation(id);
        users.remove(id);
        return "пользователь с id: " + id + " удален";
    }

    public User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException {
        idValidation(id);
        idValidation(friendId);

        User user = users.get(id);
        Set<Long> userFriendsIds = user.getFriendIds();
        if (friendsListIsEmpty(userFriendsIds)) {
            userFriendsIds = new HashSet<>();
        }
        userFriendsIds.add(friendId);
        user.setFriendIds(userFriendsIds);

        User friend = users.get(friendId);
        Set<Long> friendFriendsIds = friend.getFriendIds();
        if (friendsListIsEmpty(friendFriendsIds)) {
            friendFriendsIds = new HashSet<>();
        }
        friendFriendsIds.add(id);
        friend.setFriendIds(friendFriendsIds);

        return user;
    }

    public User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException, FriendsListNotFoundException {
        idValidation(id);
        idValidation(friendId);

        User user = users.get(id);
        Set<Long> userFriendsIds = user.getFriendIds();
        friendsListValidation(userFriendsIds, friendId);
        userFriendsIds.remove(friendId);

        User friend = users.get(friendId);
        Set<Long> friendFriendsIds = friend.getFriendIds();
        friendsListValidation(friendFriendsIds, id);
        friendFriendsIds.remove(id);

        return user;
    }

    public List<User> getFriends(long id) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        idValidation(id);
        User user = users.get(id);

        Set<Long> friendsIds = user.getFriendIds();
        friendsListValidation(friendsIds);

        List<User> friendsList = new ArrayList<>();
        for (Long friendId : friendsIds) {
            friendsList.add(users.get(friendId));
        }

        return friendsList;
    }

    public List<User> getCommonFriends(long id, long otherId) throws ValidationException, IdNotFoundException, FriendsListNotFoundException {
        idValidation(id);
        idValidation(otherId);

        User user = users.get(id);
        Set<Long> userFriendIds = user.getFriendIds();

        User otherUser = users.get(otherId);
        Set<Long> otherUserFriendsIds = otherUser.getFriendIds();

        List<User> commonFriendsList = new ArrayList<>();

        if (!friendsListIsEmpty(userFriendIds) && !friendsListIsEmpty(otherUserFriendsIds)) {
            for (Long userFriendId : userFriendIds) {
                for (Long otherUserFriendId : otherUserFriendsIds) {
                    if (userFriendId != null && otherUserFriendId != null && userFriendId.equals(otherUserFriendId)) {
                        commonFriendsList.add(users.get(userFriendId));
                    }
                }
            }
        }
        return commonFriendsList;
    }

    public boolean likedFilmsListIsEmpty(Set<Long> likedFilms) {
        if (likedFilms == null) {
            return true;
        }
        return false;
    }

    public void likedFilmsListValidation(Set<Long> likes, long filmId) throws ValidationException {
        if (likedFilmsListIsEmpty(likes)) {
            throw new FriendsListNotFoundException("у пользователя нет лайков");
        }
        if (!likes.contains(filmId)) {
            throw new ValidationException("у пользователя нет лайка для фильма с filmId: " + filmId);
        }
    }

    private long createID() {
        return ++currentID;
    }

    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }
        if (!users.containsKey(id)) {
            throw new IdNotFoundException("пользователь с id: " + id + " не существует");
        }
    }

    private void emailValidation(String email) throws ValidationException {
        if (email != null) {
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    throw new ValidationException("пользователь с таким email уже существует");
                }
            }
            if (email.isBlank() || !email.contains("@")) {
                throw new ValidationException("некорректный email. ваш email: " + email);
            }
        } else {
            throw new ValidationException("поле \"mail\" должно быть заполнено ваш email: " + email);
        }
    }

    private void loginValidation(String login) throws ValidationException {
        if (login != null) {
            if (login.isBlank() || login.contains(" ")) {
                throw new ValidationException("некорректный login");
            }
        } else {
            throw new ValidationException("поле \"login\" должно быть заполнено");
        }
    }

    private void birthdayValidation(LocalDate birthday) throws ValidationException {
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                throw new ValidationException("birthday не может быть в будущем");
            }
        } else {
            throw new ValidationException("поле \"birthday\" должно быть заполнено");
        }
    }

    private boolean nameValidationFailed(String name) {
        if (name == null || name.isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    private void chooseLoginOrName(User user, String name, String login) {
        if (nameValidationFailed(name)) {
            user.setName(login);
        } else {
            user.setName(name);
        }
    }

    private boolean friendsListIsEmpty(Set<Long> friends) {
        if (friends == null) {
            return true;
        }
        return false;
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
