package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film newFilm) throws ValidationException;

    Film getById(long id) throws ValidationException;

    List<Film> getFilmsList();

    Film updateFilm(Film updatedFilm) throws ValidationException;

    String deleteFilm(long id) throws ValidationException;
}
