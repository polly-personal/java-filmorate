package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private final UserService userService;

    @Autowired
    public FilmServiceImpl(FilmDao filmDao, LikeDao likeDao, UserService userService) {
        this.filmDao = filmDao;
        this.likeDao = likeDao;
        this.userService = userService;
    }

    @Override
    public Film createFilm(Film newFilm) throws ValidationException {
        filmValidation(newFilm);
        return filmDao.createFilm(newFilm);
    }

    @Override
    public Film getById(long id) throws ValidationException {
        idValidation(id);
        return filmDao.getById(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmDao.getAllFilms();
    }

    @Override
    public Film updateFilm(Film updatedFilm) throws ValidationException {
        idValidation(updatedFilm.getId());
        filmValidation(updatedFilm);

        return filmDao.updateFilm(updatedFilm);
    }

    @Override
    public String deleteFilm(long id) throws ValidationException {
        return filmDao.deleteFilm(id);
    }

    @Override
    public void addLike(long id, long userId) throws ValidationException {
        idValidation(id);
        userService.idValidation(userId);

        likeDao.addLike(id, userId);
    }

    @Override
    public void deleteLike(long id, long userId) throws ValidationException {
        idValidation(id);
        userService.idValidation(userId);

        likeDao.deleteLike(id, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        return filmDao.getPopular(count);
    }

    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }
    }

    private void filmValidation(Film film) {
        descriptionValidation(film.getDescription());
        releaseDateValidation(film.getReleaseDate());
        durationValidation(film.getDuration());
    }

    private void descriptionValidation(String description) throws ValidationException {
        if (description != null) {
            if (description.isBlank()) {
                throw new ValidationException("некорректный description");
            }
        }
    }

    private void releaseDateValidation(LocalDate releaseDate) throws ValidationException {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("\"releaseDate\" не может быть раньше, чем 1895/12/28");
        }
    }

    private void durationValidation(double duration) throws ValidationException {
        if (duration < 0.1) {
            throw new ValidationException("поле \"duration\" не может быть отрицательным или равно нулю");
        }
    }

    private void countForPopularValidation(int count) {
        if (count <= 0) {
            throw new ValidationException("некорректный параметр запроса -- count: " + count);
        }
    }
}
