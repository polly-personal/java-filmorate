package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film createFilm(Film newFilm) throws ValidationException;

    public Film getById(long id) throws ValidationException;

    public List<Film> getFilmsList();

    public Film updateFilm(Film updatedFilm) throws ValidationException;

    public String deleteFilm(long id) throws ValidationException;
}
