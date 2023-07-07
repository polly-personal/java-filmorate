package ru.yandex.practicum.filmorate.manager;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class UsersManager {
    int currentID;

    private HashMap<Integer, User> users = new HashMap();

    public int createID() {
        ++currentID;
        return currentID;
    }

    public User createUser(User newUser) throws ValidationException {
        String userEmail = newUser.getEmail();
        emailValidation(userEmail);

        String userLogin = newUser.getLogin();
        loginValidation(userLogin);

        LocalDate userBirthday = newUser.getBirthday();
        birthdayValidation(userBirthday);

        User user = User.builder()
                .id(createID())
                .email(userEmail)
                .login(userLogin)
                .birthday(userBirthday)
                .build();

        String userName = newUser.getName();
        if (nameValidationFailed(userName)) {
            user.setName(userLogin);
        } else {
            user.setName(userName);
        }

        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User updatedUser) throws ValidationException {
        int id = updatedUser.getId();
        if (!users.containsKey(id)) {
            throw new ValidationException("пользователь с id: " + id + " не существует!");
        }

        String updatedEmail = updatedUser.getEmail();
        emailValidation(updatedEmail);

        String updatedLogin = updatedUser.getLogin();
        loginValidation(updatedLogin);

        LocalDate updatedBirthday = updatedUser.getBirthday();
        birthdayValidation(updatedBirthday);

        User userByID = users.get(id);

        String updatedName = updatedUser.getName();
        if (nameValidationFailed(updatedName)) {
            userByID.setName(updatedLogin);
        } else {
            userByID.setName(updatedName);
        }

        userByID.setEmail(updatedEmail);
        userByID.setLogin(updatedLogin);
        userByID.setBirthday(updatedBirthday);

        return userByID;
    }

    public void emailValidation(String email) {
        if (email != null) {
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    throw new ValidationException("пользователь с таким email уже существует!");
                }
            }
            if (email.isBlank() || !email.contains("@")) {
                throw new ValidationException("некорректный email! ваш email: " + email);
            }
        } else {
            throw new ValidationException("поле \"mail\" должно быть заполнено! ваш email: " + email);
        }
    }

    public void loginValidation(String login) {
        if (login != null) {
            if (login.isBlank() || login.contains(" ")) {
                throw new ValidationException("некорректный login");
            }
        } else {
            throw new ValidationException("поле \"login\" должно быть заполнено!");
        }
    }

    public void birthdayValidation(LocalDate birthday) {
        if (birthday != null) {
            if (birthday.isAfter(LocalDate.now())) {
                throw new ValidationException("birthday не может быть в будущем");
            }
        } else {
            throw new ValidationException("поле \"birthday\" должно быть заполнено!");
        }
    }

    public boolean nameValidationFailed(String name) {
        if (name == null || name.isBlank()) {
            return true;
        } else {
            return false;
        }
    }

    public List<User> getUsersList() {
        List<User> list = new ArrayList<>(users.values());
        return list;
    }
}
