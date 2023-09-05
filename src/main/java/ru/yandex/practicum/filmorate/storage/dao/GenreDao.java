package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre getById(long id) throws IdNotFoundException;

    List<Genre> getGenreList() throws SQLException;

    Set<Genre> getGenresByFilmId(long id);

    boolean idIsExists(long id) throws ValidationException, IdNotFoundException;
}
