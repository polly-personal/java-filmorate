package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.List;

public interface FilmService {
    Film createFilm(Film newFilm) throws ValidationException;

    Film getById(long id) throws IdNotFoundException;

    List<Film> getAllFilms() throws SQLException;

    Film updateFilm(Film updatedFilm) throws IdNotFoundException, ValidationException;

    String deleteFilm(long id) throws IdNotFoundException;

    void addLike(long id, long userId) throws IdNotFoundException;

    void deleteLike(long id, long userId) throws IdNotFoundException;

    List<Film> getPopular(int count) throws SQLException, ValidationException;

    boolean idIsValid(long id) throws IdNotFoundException;

    void filmValidation(Film film) throws ValidationException;
}
