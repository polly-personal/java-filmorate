package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.constant.LogType;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionForResponse;
import ru.yandex.practicum.filmorate.manager.FilmsManager;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Managers;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private FilmsManager filmsManager = Managers.getDefaultFilmsManager();

    @GetMapping
    public List<Film> getFilmsList() {
        List<Film> films = filmsManager.getFilmsList();
        log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + filmsManager.getFilms());
        log.info("🟩 список фильнов выдан: " + films);
        return films;
    }

    @PostMapping
    public Film createUser(@RequestBody Film newFilm) throws ValidationException, ValidationExceptionForResponse {
        try {
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + filmsManager.getFilms().size());
            Film createdFilm = filmsManager.createFilm(newFilm);

            log.info("🟩 добавлен фильм: " + createdFilm);
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + filmsManager.getFilms().size());
            return createdFilm;

        } catch (ValidationException e) {
            log.info("🟩 фильм НЕ добавлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws ValidationException, ValidationExceptionForResponse{
        try {
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + filmsManager.getFilms().size());
            Film currentFilm = filmsManager.updateFilm(updatedFilm);

            log.info("🟩 фильм обновлен: " + currentFilm);
            log.debug(getTextForLog(LogType.DEBUG_CURRENT_NUMBER_OF_USERS) + filmsManager.getFilms().size());
            return currentFilm;

        } catch (ValidationException e) {
            log.info("🟩 фильм НЕ обновлен");
            log.warn("🟥" + e.getMessage());
            System.out.println("⬛️" + e.getMessage());
            throw new ValidationExceptionForResponse();
        }
    }

    public String getTextForLog(LogType logType) {
        if (logType.equals(LogType.DEBUG_CURRENT_NUMBER_OF_USERS)) {
            return "🟧 текущее количество пользователей: ";
        }
        return null;
    }
}
