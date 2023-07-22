package ru.yandex.practicum.filmorate.service.film;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
public class FilmService implements FilmStorage {
    long currentID;

    private Map<Long, Film> films = new HashMap();

    private UserService userService;

    @Autowired
    public FilmService(UserService userService) {
        this.userService = userService;
    }

    public Film createFilm(Film newFilm) throws ValidationException {
        nameValidation(newFilm.getName());
        descriptionValidation(newFilm.getDescription());
        releaseDateValidation(newFilm.getReleaseDate());
        durationValidation(newFilm.getDuration());

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

        nameValidation(updatedFilm.getName());
        descriptionValidation(updatedFilm.getDescription());
        releaseDateValidation(updatedFilm.getReleaseDate());
        durationValidation(updatedFilm.getDuration());

        films.put(id, updatedFilm);
        return updatedFilm;
    }

    public String deleteFilm(long id) throws ValidationException {
        idValidation(id);
        films.remove(id);
        return "фильм с id: " + id + " удален";
    }

    public Film addLike(long id, long userId) throws ValidationException {
        idValidation(id);
        userService.idValidation(userId);

        Film film = films.get(id);
        Set<Long> likes = film.getLikes();
        if (likeListIsEmpty(likes)) {
            likes = new HashSet<>();
        }
        likes.add(userId);
        film.setLikes(likes);

        User user = userService.getUsers().get(userId);
        Set<Long> likedFilms = user.getLikedFilms();
        if (userService.likedFilmsListIsEmpty(likedFilms)) {
            likedFilms = new HashSet<>();
        }
        likedFilms.add(id);
        user.setLikedFilms(likedFilms);

        return film;
    }

    public Film deleteLike(long id, long userId) throws ValidationException {
        idValidation(id);
        userService.idValidation(userId);

        Film film = films.get(id);
        Set<Long> filmLikes = film.getLikes();
        likesListValidation(filmLikes, userId);
        filmLikes.remove(userId);

        User user = userService.getUsers().get(userId);
        Set<Long> userLikes = user.getLikedFilms();
        userService.likedFilmsListValidation(userLikes, id);
        userLikes.remove(id);

        return film;
    }

    public List<Film> getPopular(int count) {
        countForPopularValidation(count);

        List<Film> pop = films.values().
                stream().
                sorted((film1, film2) -> {
                    int comp;
                    if (film1.getLikes() == null || film2.getLikes() == null) {
                        comp = 0;
                    } else {
                        comp = film1.getLikes().size() - film2.getLikes().size();
                    }
                    return comp * -1;
                })
                .collect(Collectors.toList());
        return pop.subList(0, count);
    }

    private long createID() {
        return ++currentID;
    }

    private void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new ValidationException("ваш id: " + id + " отрицательный либо равен 0");
        }
        if (!films.containsKey(id)) {
            throw new IdNotFoundException("фильм с id: " + id + " не существует");
        }
    }

    private void nameValidation(String name) throws ValidationException {
        if (name != null) {
            if (name.isBlank()) {
                throw new ValidationException("поле \"name\" должно быть заполнено");
            }
        } else {
            throw new ValidationException("поле \"name\" должно быть заполнено");
        }
    }

    private void descriptionValidation(String description) throws ValidationException {
        if (description != null) {
            if (description.isBlank()) {
                throw new ValidationException("некорректный description");
            }
            if (description.length() > 200) {
                throw new ValidationException("длина description больше 200 символов");
            }
        }
    }

    private void releaseDateValidation(LocalDate releaseDate) throws ValidationException {
        if (releaseDate != null) {
            if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
                throw new ValidationException("\"releaseDate\" не может быть раньше, чем 1895/12/28");
            }
        } else {
            throw new ValidationException("поле \"releaseDate\" должно быть заполнено");
        }
    }

    private void durationValidation(double duration) throws ValidationException {
        if (duration < 0.1) {
            throw new ValidationException("поле \"duration\" не может быть отрицательным или равно нулю");
        }
    }

    private boolean likeListIsEmpty(Set<Long> likes) {
        if (likes == null) {
            return true;
        }
        return false;
    }

    private void likesListValidation(Set<Long> likes, long userId) throws ValidationException {
        if (likeListIsEmpty(likes)) {
            throw new FriendsListNotFoundException("у фильма нет лайков");
        }
        if (!likes.contains(userId)) {
            throw new ValidationException("у фильма нет лайка с userId: " + userId);
        }
    }

    private void countForPopularValidation(int count) {
        if (count <= 0) {
            throw new ValidationException("некорректный параметр запроса count: " + count);
        }
        if (count > films.size()) {
            throw new ValidationException("параметр запроса count: " + count + " -- больше, чем существущих фильмов на данный момент: " + films.size());
        }
    }
}
