package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.friendship.FriendshipService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    @Autowired
    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) throws ValidationException {
        User user = userService.createUser(newUser);
        log.info("🟩 добавлен пользователь: " + newUser);
        return user;
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable long id) throws IdNotFoundException {
        User user = userService.getById(id);
        log.info("🟩 выдан пользователь: " + user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        log.info("🟩 список пользователей выдан: " + users);
        return users;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User updatedUser) throws IdNotFoundException, ValidationException {
        User user = userService.updateUser(updatedUser);
        log.info("🟩 пользователь обновлен: " + user);
        return user;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable long id) throws IdNotFoundException {
        String responseMessage = userService.deleteUser(id);
        log.info("🟩 удален пользователь по id: " + id);
        return responseMessage;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) throws IdNotFoundException {
        friendshipService.addFriendOnlyForUser(id, friendId);
        User user = userService.getById(id);
        log.info("🟩 пользователю по id: " + id + ", добавлен друг: " + userService.getById(friendId) + " по friendId:" + friendId);

        return user;
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) throws IdNotFoundException {
        friendshipService.deleteFriendForUserOnly(id, friendId);
        User user = userService.getById(id);
        log.info("🟩 у пользователя по id: " + id + ", удален друг по friendId: " + friendId);
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) throws IdNotFoundException,
            FriendsListNotFoundException, SQLException {
        List<User> friends = friendshipService.getUserFriends(id);
        log.info("🟩 выдан список друзей: " + friends + " пользователя с id: " + id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) throws
            IdNotFoundException, FriendsListNotFoundException, SQLException {
        List<User> friends = friendshipService.getCommonFriends(id, otherId);
        log.info("🟩 выдан список общих друзей: " + friends + " пользователей с id: " + id + " и otherId: " + otherId);
        return friends;
    }
}
