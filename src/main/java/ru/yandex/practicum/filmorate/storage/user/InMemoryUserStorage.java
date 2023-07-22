package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class InMemoryUserStorage implements UserStorage {
    private long currentID;

    private Map<Long, User> users = new HashMap();

    public User createUser(User newUser) {
        newUser.setId(createID());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User getById(long id) {
        return users.get(id);
    }

    public List<User> getUsersList() {
        List<User> list = new ArrayList<>(users.values());
        return list;
    }

    public User updateUser(User updatedUser) {
        users.put(updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    public String deleteUser(long id) {
        users.remove(id);
        return "пользователь с id: " + id + " удален";
    }

    private long createID() {
        return ++currentID;
    }
}
