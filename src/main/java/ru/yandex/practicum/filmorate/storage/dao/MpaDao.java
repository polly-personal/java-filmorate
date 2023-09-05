package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.SQLException;
import java.util.List;

public interface MpaDao {
    Mpa getById(long id);

    List<Mpa> getMpaList() throws SQLException;

    boolean idIsExists(long id);
}
