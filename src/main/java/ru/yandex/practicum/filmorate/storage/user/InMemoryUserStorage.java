package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private long currentID;

    private Map<Long, User> users = new HashMap();

    public User createUser(User newUser) throws ValidationException {
        userValidation(newUser);

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

        userValidation(updatedUser);

        users.put(id, updatedUser);
        return updatedUser;
    }

    public String deleteUser(long id) throws IdNotFoundException, ValidationException {
        idValidation(id);
        users.remove(id);
        return "пользователь с id: " + id + " удален";
    }


    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }
        if (!users.containsKey(id)) {
            throw new IdNotFoundException("пользователь с id: " + id + " не существует");
        }
    }

    private long createID() {
        return ++currentID;
    }

    private void userValidation(User user) {
        emailValidation(user.getEmail());
        birthdayValidation(user.getBirthday());

        String userLogin = user.getLogin();
        chooseLoginOrName(user, user.getName(), userLogin);
    }

    private void emailValidation(String email) throws ValidationException {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                throw new ValidationException("пользователь с таким email уже существует");
            }
        }
    }

    private void birthdayValidation(LocalDate birthday) throws ValidationException {
        if (birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("birthday не может быть в будущем");
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
}
