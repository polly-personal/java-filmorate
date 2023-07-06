package ru.yandex.practicum.filmorate.manager;

import lombok.Data;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class FilmsManager {
    int currentID;

    private final HashMap<Integer, Film> films = new HashMap();

    public int createID() {
        ++currentID;
        return currentID;
    }

    public Film createFilm(Film newFilm) throws ValidationException, NullPointerException {
        String filmName = newFilm.getName();
        nameValidation(filmName);

        String filmDescription = newFilm.getDescription();
        descriptionValidation(filmDescription);

        LocalDate filmRelease = newFilm.getReleaseDate();
        releaseDateValidation(filmRelease);

        int filmDuration = newFilm.getDuration();
        durationValidation(filmDuration);

        Film film = Film.builder()
                .id(createID())
                .name(filmName)
                .description(filmDescription)
                .releaseDate(filmRelease)
                .duration(filmDuration)
                .build();

        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException{
        int ID = updatedFilm.getId();
        if (!films.containsKey(ID)) {
            throw new ValidationException("пользователь с ID: " + ID + " не существует!");
        }

        Film currentFilm = films.get(updatedFilm.getId());

        String updatedName = updatedFilm.getName();
        nameValidation(updatedName);
        currentFilm.setName(updatedName);

        String updatedDescription = updatedFilm.getDescription();
        descriptionValidation(updatedDescription);
        currentFilm.setDescription(updatedDescription);

        LocalDate updatedReleaseDate = updatedFilm.getReleaseDate();
        releaseDateValidation(updatedReleaseDate);
        currentFilm.setReleaseDate(updatedReleaseDate);

        int updatedDuration = updatedFilm.getDuration();
        durationValidation(updatedDuration);
        currentFilm.setDuration(updatedDuration);

        films.put(ID, currentFilm);
        return currentFilm;
    }

    public void nameValidation(String name) throws ValidationException{
        if (name != null) {
            if (name.equals("null") || name.isBlank()) {
                throw new ValidationException("поле \"name\" должно быть заполнено!");
            }
            for (Film film : films.values()) {
                if (film.getName().equals(name)) {
                    throw new ValidationException("фильм с таким именем уже существует!");
                }
            }
        } else {
            throw new ValidationException("поле \"name\" должно быть заполнено!");
        }
    }

    public void descriptionValidation(String description) {
        if (description != null) {
            if (description.isBlank()) {
                throw new ValidationException("некорректное description");
            }
            if (description.length() > 200) {
                throw new ValidationException("длина description больше 200 символов!");
            }
        }
    }

    public void releaseDateValidation(LocalDate releaseDate) {
        if (releaseDate != null) {
            if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("\"releaseDate\" не может быть раньше, чем 1895/12/28");
            }
        } else {
            throw new ValidationException("поле \"releaseDate\" должно быть заполнено!");
        }
    }

    public void durationValidation(int duration) {
        if (duration < 0) {
            throw new ValidationException("поле \"duration\" не может быть отрицательным!");
        }
    }

    public List<Film> getFilmsList() {
        List<Film> list = new ArrayList<>();
        for (Film film : films.values()) {
            list.add(film);
        }
        return list;
    }
}
