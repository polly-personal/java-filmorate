package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/{id}")
    public Mpa getById(@PathVariable long id) throws IdNotFoundException {
        Mpa mpa = this.mpaService.getById(id);
        log.info("🟩 выдан mpa-рейтинг: " + mpa);
        return mpa;
    }

    @GetMapping
    public List<Mpa> getFilmsList() throws SQLException {
        List<Mpa> mpas = mpaService.getMpaList();
        log.info("🟩 список mpa-рейтингов выдан: " + mpas);
        return mpas;
    }
}
