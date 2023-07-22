package ru.yandex.practicum.filmorate.storage.film;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class InMemoryFilmStorage implements FilmStorage {
    long currentID;

    private Map<Long, Film> films = new HashMap();

    public Film createFilm(Film newFilm) {
        newFilm.setId(createID());
        films.put(newFilm.getId(), newFilm);

        return newFilm;
    }

    public Film getById(long id) {
        return films.get(id);
    }

    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    public Film updateFilm(Film updatedFilm) {
        films.put(updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    public String deleteFilm(long id) {
        films.remove(id);
        return "фильм с id: " + id + " удален";
    }

    private long createID() {
        return ++currentID;
    }
}
