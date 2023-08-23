package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable long id) throws IdNotFoundException {
        Genre genre = genreService.getById(id);
        log.info("🟩 выдан жанр: " + genre);
        return genre;
    }

    @GetMapping
    public List<Genre> getFilmsList() throws SQLException {
        List<Genre> genres = genreService.getGenresList();
        log.info("🟩 список жанров выдан: " + genres);
        return genres;
    }
}
