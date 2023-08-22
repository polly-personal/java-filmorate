package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("LikeDaoTest должен ")
class LikeDaoTest {
    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private final UserDao userDao;
//    private final ManagerDatabaseDao managerDatabaseDao;
    private User defaultUser;
    private Film defaultFilm1;
    private Film defaultFilm2;

    @BeforeEach
    public void createDefaultUserAndTwoDefaultFilms() {
        defaultUser = userDao.createUser(
                User.builder()
                        .email("email@mail.ru")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2010, 1, 1))
                        .build()
        );

        Genre genre = Genre.builder().id(1).name("Комедия").build();
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        defaultFilm1 = filmDao.createFilm(
                Film.builder()
                        .name("name_1")
                        .releaseDate(LocalDate.of(2010, 1, 1))
                        .duration(160)
                        .description("description_1")
                        .rate(1)
                        .mpa(Mpa.builder().id(1).name("G").build())
                        .genres(genres)
                        .build()
        );

        defaultFilm2 = filmDao.createFilm(
                Film.builder()
                        .name("name_2")
                        .releaseDate(LocalDate.of(2011, 1, 1))
                        .duration(160)
                        .description("description_2")
                        .rate(2)
                        .mpa(Mpa.builder().id(1).name("G").build())
                        .genres(genres)
                        .build()
        );
    }

//    @AfterEach
//    public void refreshAllTabs() {
//        managerDatabaseDao.deleteAllTabs();
//        managerDatabaseDao.createAllTabs();
//    }

    @DisplayName("добавить лайк фильму от пользователя")
    @Test
    public void addLike() {
        User returnedUser = defaultUser;
        Film returnedFilm1 = defaultFilm1;
        Film returnedFilm2 = defaultFilm2;

        likeDao.addLike(returnedFilm2.getId(), returnedUser.getId());
        List<Film> returnedPopularFilms = filmDao.getPopular(1);

        Film returnedPopularFilm = returnedPopularFilms.get(0);
        assertEquals(2, returnedPopularFilm.getId(), "id популярного фильма != 2");
    }

    @DisplayName("удалить лайк для фильму от пользователя")
    @Test
    public void deleteLike() {
        User returnedUser = defaultUser;
        Film returnedFilm1 = defaultFilm1;
        Film returnedFilm2 = defaultFilm2;

        likeDao.addLike(returnedFilm2.getId(), returnedUser.getId());
        likeDao.deleteLike(returnedFilm2.getId(), returnedUser.getId());

        List<Film> returnedPopularFilms = filmDao.getPopular(10);
        assertEquals(2, returnedPopularFilms.size(), "size списка популярных фильмов != 2");
    }
}