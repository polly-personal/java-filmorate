package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.LogType;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionForResponse;
import ru.yandex.practicum.filmorate.manager.UsersManager;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Managers;

import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private UsersManager usersManager = Managers.getDefaultUsersManager();

    @GetMapping
    public List<User> getUsersList() {
        List<User> users = usersManager.getUsersList();
        log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + usersManager.getUsers().size());
        log.info("🟩 список пользователей выдан: " + users);
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) throws ValidationException, ValidationExceptionForResponse {
        try {
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + usersManager.getUsers().size());
            User createdUser = usersManager.createUser(newUser);

            log.info("🟩 добавлен пользователь: " + createdUser);
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + usersManager.getUsers().size());
            return createdUser;

        } catch (ValidationException e) {
            log.info("🟩 пользователь НЕ добавлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User updatedUser) throws ValidationException, ValidationExceptionForResponse {
        try {
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + usersManager.getUsers().size());
            User currentUser = usersManager.updateUser(updatedUser);

            log.info("🟩 пользователь обновлен: " + currentUser);
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + usersManager.getUsers().size());
            return currentUser;

        } catch (ValidationException e) {
            log.info("🟩 пользователь НЕ обновлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    public String getTextForLog(LogType logType) {
        if (logType.equals(LogType.DEBUG_CURRENT_NUMBER_OF_USERS)) {
            return "🟧 текущее количество пользователей: ";
        }
        return null;
    }
}
