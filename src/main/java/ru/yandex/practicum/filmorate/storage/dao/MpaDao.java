package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    Mpa getById(long id);

    List<Mpa> getMpaList();

    void idValidation(long id) throws ValidationException, IdNotFoundException;
}
