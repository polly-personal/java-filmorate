package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.List;

public interface FilmDao {
    Film createFilm(Film newFilm);

    Film getById(long id) throws IdNotFoundException;

    List<Film> getAllFilms() throws SQLException;

    Film updateFilm(Film updatedFilm);

    String deleteFilm(long id);

    List<Film> getPopular(int count) throws SQLException;

    boolean idIsExists(long id);
}
