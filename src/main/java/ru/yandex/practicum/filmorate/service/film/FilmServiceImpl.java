package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.impl.FilmDaoImpl;

import java.util.*;

@Service
public class FilmServiceImpl implements FilmService {
    private FilmDao filmDao;
    private UserServiceImpl userServiceImpl;

    @Autowired
    public FilmServiceImpl(FilmDaoImpl filmDao, UserServiceImpl userServiceImpl) {
        this.filmDao = filmDao;
        this.userServiceImpl = userServiceImpl;
    }

    public Film createFilm(Film newFilm) throws ValidationException {
        return filmDao.createFilm(newFilm);
    }

    public Film getById(long id) throws ValidationException {
        return filmDao.getById(id);
    }

    public List<Film> getFilmsList() {
        return filmDao.getFilmsList();
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException {
        return filmDao.updateFilm(updatedFilm);
    }

    public String deleteFilm(long id) throws ValidationException {
        return filmDao.deleteFilm(id);
    }

    public Film addLike(long id, long userId) throws ValidationException {
        return filmDao.addLike(id, userId);
    }

    public Film deleteLike(long id, long userId) throws ValidationException {
        return filmDao.deleteLike(id, userId);
    }

    public List<Film> getPopular(int count) {
        return filmDao.getPopular(count);
    }
}
