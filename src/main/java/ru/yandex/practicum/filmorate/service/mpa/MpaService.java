package ru.yandex.practicum.filmorate.service.mpa;

import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.List;

public interface MpaService {
    Mpa getById(long id) throws IdNotFoundException;

    List<Mpa> getMpaList() throws SQLException;

    boolean idIsValid(long id) throws IdNotFoundException;
}
