package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long currentID;

    private Map<Long, Film> films = new HashMap();

    public Film createFilm(Film newFilm) throws ValidationException {
        filmValidation(newFilm);

        newFilm.setId(createID());
        films.put(newFilm.getId(), newFilm);

        return newFilm;
    }

    public Film getById(long id) throws ValidationException {
        idValidation(id);
        return films.get(id);
    }

    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException {
        long id = updatedFilm.getId();
        idValidation(id);

        filmValidation(updatedFilm);

        films.put(id, updatedFilm);
        return updatedFilm;
    }

    public String deleteFilm(long id) throws ValidationException {
        idValidation(id);
        films.remove(id);
        return "фильм с id: " + id + " удален";
    }


    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }
        if (!films.containsKey(id)) {
            throw new IdNotFoundException("фильм с id: " + id + " не существует");
        }
    }

    private long createID() {
        return ++currentID;
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

}
