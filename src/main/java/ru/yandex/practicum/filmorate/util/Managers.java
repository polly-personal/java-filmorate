package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

public class Managers {
    public static InMemoryUserStorage getDefaultInMemoryUserStorage() {
        return new InMemoryUserStorage();
    }

    public static InMemoryFilmStorage getDefaultInMemoryFilmStorage() {
        return new InMemoryFilmStorage();
    }
}
