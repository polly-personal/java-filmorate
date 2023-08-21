package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDao {
    Film createFilm(Film newFilm) throws ValidationException;

    Film getById(long id) throws ValidationException;

    List<Film> getFilmsList();

    Film updateFilm(Film updatedFilm) throws ValidationException;

    String deleteFilm(long id) throws ValidationException;

    Film addLike(long id, long userId);

    Film deleteLike(long id, long userId);

    List<Film> getPopular(int count);

    void idValidation(long id) throws ValidationException, IdNotFoundException;
}
