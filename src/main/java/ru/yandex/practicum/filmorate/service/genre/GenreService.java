package ru.yandex.practicum.filmorate.service.genre;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.SQLException;
import java.util.List;

public interface GenreService {
    Genre getById(long id) throws IdNotFoundException;

    List<Genre> getGenresList() throws SQLException;

    boolean idIsValid(long id) throws IdNotFoundException;
}
