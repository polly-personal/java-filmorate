package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreDao {
    Genre getById(long id);

    List<Genre> getGenreList();

    Set<Genre> getGenresByFilmId(long id);

    void idValidation(long id) throws ValidationException, IdNotFoundException;
}
