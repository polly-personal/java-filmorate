package ru.yandex.practicum.filmorate.manager;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Managers;

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

        User currentUser = users.get(updatedUser.getId());

        String updatedEmail = updatedUser.getEmail();
        emailValidation(updatedEmail);
        currentUser.setEmail(updatedEmail);

        String updatedLogin = updatedUser.getLogin();
        loginValidation(updatedLogin);
        currentUser.setLogin(updatedLogin);

        LocalDate updatedBirthday = updatedUser.getBirthday();
        birthdayValidation(updatedBirthday);
        currentUser.setBirthday(updatedBirthday);

        String updatedName = updatedUser.getName();
        if (nameValidationFailed(updatedName)) {
            currentUser.setName(updatedLogin);
        } else {
            currentUser.setName(updatedName);
        }

        users.put(id, currentUser);
        return currentUser;
    }

    public void emailValidation(String email) {
        if (email != null) {
            if (email.equals("null")) {
                throw new ValidationException("поле \"email\" должно быть заполнено!");
            }
            for (User user : users.values()) {
                if (user.getEmail().equals(email)) {
                    throw new ValidationException("пользователь с такой email уже существует!");
                }
            }
            if (email.isBlank() || !email.contains("@")) {
                throw new ValidationException("некорректная email");
            }
        } else {
            throw new ValidationException("поле \"mail\" должно быть заполнено!");
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
        if (name == null) {
            return true;
        } else {
            if (name.isBlank()) {
                return true;
            }
            return false;
        }
    }

    public List<User> getUsersList() {
        List<User> list = new ArrayList<>();
        for (User user : users.values()) {
            list.add(user);
        }
        return list;
    }

    public static void main(String[] args) {
        UsersManager usersManager = Managers.getDefaultUsersManager();

        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("userEmail@")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        usersManager.createUser(user);

        User user2 = User.builder()
                .email("polly@")
                .login("pollyLogin")
                .name("userPolly")
                .build();
        usersManager.createUser(user2);

        System.out.println(usersManager.getUsersList());
    }
}
