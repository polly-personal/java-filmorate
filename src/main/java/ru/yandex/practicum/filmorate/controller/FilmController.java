package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) throws ValidationException {
        Film film = filmService.createFilm(newFilm);
        log.info("🟩 добавлен фильм: " + film);
        return film;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) throws IdNotFoundException {
        Film film = filmService.getById(id);
        log.info("🟩 выдан фильм: " + film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() throws SQLException {
        List<Film> films = filmService.getAllFilms();
        log.info("🟩 список фильмов выдан: " + films);
        return films;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) throws IdNotFoundException, ValidationException {
        Film film = filmService.updateFilm(updatedFilm);
        log.info("🟩 фильм обновлен: " + film);
        return film;
    }

    @DeleteMapping("/{id}")
    public String deleteFilm(@PathVariable long id) throws IdNotFoundException {
        String responseMessage = filmService.deleteFilm(id);
        log.info("🟩 удален фильм по id: " + id);
        return responseMessage;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) throws IdNotFoundException {
        filmService.addLike(id, userId);
        log.info("🟩 фильму по id: " + id + ", добавлен лайк: " + filmService.getById(id) + " по userId:" + userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) throws IdNotFoundException {
        filmService.deleteLike(id, userId);
        log.info("🟩 для фильма по id: " + id + ", удален лайк: " + filmService.getById(id) + " по userId:" + userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) throws SQLException, ValidationException {
        List<Film> films = filmService.getPopular(count);
        log.info("🟩 выдан список популярных фильмов");

        return films;
    }
}
