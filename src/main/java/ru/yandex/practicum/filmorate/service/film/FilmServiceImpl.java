package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

@Service
public class FilmServiceImpl implements FilmService {
    private InMemoryFilmStorage filmStorage;
    private InMemoryUserStorage userStorage;
    private UserServiceImpl userServiceImpl;

    @Autowired
    public FilmServiceImpl(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage, UserServiceImpl userServiceImpl) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userServiceImpl = userServiceImpl;
    }

    public Film createFilm(Film newFilm) throws ValidationException {
        return filmStorage.createFilm(newFilm);
    }

    public Film getById(long id) throws ValidationException {
        return filmStorage.getById(id);
    }

    public List<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public Film updateFilm(Film updatedFilm) throws ValidationException {
        return filmStorage.updateFilm(updatedFilm);
    }

    public String deleteFilm(long id) throws ValidationException {
        return filmStorage.deleteFilm(id);
    }

    public Film addLike(long id, long userId) throws ValidationException {
        filmStorage.idValidation(id);
        userStorage.idValidation(userId);

        Film film = getFilmsList().stream()
                .filter(filmFromList -> filmFromList.getId() == id)
                .findFirst()
                .get();
        Set<Long> likes = film.getLikes();
        if (likeListIsEmpty(likes)) {
            likes = new HashSet<>();
        }
        likes.add(userId);
        film.setLikes(likes);

        User user = userStorage.getUsersList().stream()
                .filter(userFromList -> userFromList.getId() == userId)
                .findFirst()
                .get();
        Set<Long> likedFilms = user.getLikedFilms();
        if (userServiceImpl.likedFilmsListIsEmpty(likedFilms)) {
            likedFilms = new HashSet<>();
        }
        likedFilms.add(id);
        user.setLikedFilms(likedFilms);

        return film;
    }

    public Film deleteLike(long id, long userId) throws ValidationException {
        filmStorage.idValidation(id);
        userStorage.idValidation(userId);

        Film film = getFilmsList().stream()
                .filter(filmFromList -> filmFromList.getId() == id)
                .findFirst()
                .get();
        Set<Long> filmLikes = film.getLikes();
        likesListValidation(filmLikes, userId);
        filmLikes.remove(userId);

        User user = userStorage.getUsersList().stream()
                .filter(userFromList -> userFromList.getId() == userId)
                .findFirst()
                .get();
        Set<Long> userLikes = user.getLikedFilms();
        userServiceImpl.likedFilmsListValidation(userLikes, id);
        userLikes.remove(id);

        return film;
    }

    public List<Film> getPopular(int count) {
        countForPopularValidation(count);
        Comparator<Film> comparator = new Comparator<Film>() {
            @Override
            public int compare(Film f1, Film f2) {
                Set<Long> likes1 = f1.getLikes();
                Set<Long> likes2 = f2.getLikes();
                int counter;

                if (likes1 != null && likes2 != null) {
                    int size1 = likes1.size();
                    int size2 = likes2.size();
                    if (size1 != size2) {
                        counter = size1 - size2;
                        return counter;
                    }
                    counter = 0;
                    return counter;
                }

                if (likes1 == null && likes2 == null) {
                    counter = (int) f1.getId() - (int) f2.getId();
                    return counter;
                }

                if (likes1 != null) {
                    counter = 1;
                    return counter;
                }
                counter = -1;
                return counter;
            }
        };

        List<Film> filmsList = new ArrayList<>(getFilmsList());
        Collections.sort(filmsList, comparator.reversed());

        if (count > filmsList.size()) {
            count = filmsList.size();
        }

        return filmsList.subList(0, count);
    }

    private boolean likeListIsEmpty(Set<Long> likes) {
        return likes == null;
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
            throw new ValidationException("некорректный параметр запроса -- count: " + count);
        }
    }
}
