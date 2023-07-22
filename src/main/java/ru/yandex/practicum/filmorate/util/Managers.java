package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

public class Managers {

    public static InMemoryUserStorage getDefaultUsersManager() {
        return new InMemoryUserStorage();
    }

    public static UserService getDefaultUsersService() {
        return new UserService();
    }

    public static FilmService getDefaultFilmService(UserService userService) {
        return new FilmService(userService);
    }

    public static InMemoryFilmStorage getDefaultFilmsManager() {
        return new InMemoryFilmStorage();
    }
}
