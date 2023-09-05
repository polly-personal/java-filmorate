package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.sql.SQLException;
import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre getById(long id) throws IdNotFoundException {
        idIsValid(id);
        return genreDao.getById(id);
    }

    @Override
    public List<Genre> getGenresList() throws SQLException {
        return genreDao.getGenreList();
    }

    @Override
    public boolean idIsValid(long id) throws IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        if (!genreDao.idIsExists(id)) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }
        return true;
    }
}
