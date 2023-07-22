package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film newFilm) throws ValidationException {
        Film film = filmService.createFilm(newFilm);
        log.info("🟩 добавлен фильм: " + film);
        return film;
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) throws ValidationException {
        Film film = filmService.getById(id);
        log.info("🟩 выдан фильм: " + film);
        return film;
    }

    @GetMapping
    public List<Film> getFilmsList() {
        List<Film> films = filmService.getFilmsList();
        log.info("🟩 список фильмов выдан: " + films);
        return films;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException {
        Film film = filmService.updateFilm(updatedFilm);
        log.info("🟩 фильм обновлен: " + film);
        return film;
    }

    @DeleteMapping("/{id}")
    public String deleteFilm(@PathVariable long id) throws ValidationException {
        String responseMessage = filmService.deleteFilm(id);
        log.info("🟩 удален фильм по id: " + id);
        return responseMessage;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        Film film = filmService.addLike(id, userId);
        log.info("🟩 фильму по id: " + id + ", добавлен лайк: " + filmService.getById(id) + " по userId:" + userId);

        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable long id, @PathVariable long userId) {
        Film film = filmService.deleteLike(id, userId);
        log.info("🟩 для фильма по id: " + id + ", удален лайк: " + filmService.getById(id) + " по userId:" + userId);

        return film;
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        List<Film> films = filmService.getPopular(count);
        log.info("🟩 выдан список популярных фильмов");

        return films;
    }
}
