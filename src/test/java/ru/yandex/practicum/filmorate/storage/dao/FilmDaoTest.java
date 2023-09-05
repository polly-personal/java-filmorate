package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("FilmDaoTest должен ")
class FilmDaoTest {
    private final UserDao userDao;
    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private User defaultUser;
    private Film defaultFilm1;
    private Film defaultFilm2;

    @BeforeEach
    public void createDefaultUserAndFilms() {
        defaultUser = User.builder()
                .email("email@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2010, 1, 1))
                .build();

        Genre genre = Genre.builder().id(1).name("Комедия").build();
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        defaultFilm1 = Film.builder()
                .name("name_1")
                .releaseDate(LocalDate.of(2010, 1, 1))
                .duration(160)
                .description("description_1")
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(genres)
                .build();

        defaultFilm2 = Film.builder()
                .name("name_2")
                .releaseDate(LocalDate.of(2011, 1, 1))
                .duration(160)
                .description("description_2")
                .rate(2)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(genres)
                .build();
    }

    @DisplayName("создать пользователя и выдать его по его id")
    @Test
    public void createFilmAndGetById() {
        Film returnedFilm = filmDao.createFilm(defaultFilm1);
        Film returnedFilmById = filmDao.getById(returnedFilm.getId());

        assertEquals(1, returnedFilmById.getId(), "id фильма != 1");
        assertEquals("name_1", returnedFilmById.getName(),
                "name фильма != name_1");
    }

    @DisplayName("выдать список пользователей")
    @Test
    public void getFilmsList() throws SQLException {
        filmDao.createFilm(defaultFilm1);
        filmDao.createFilm(defaultFilm2);
        List<Film> returnedFilmsList = filmDao.getAllFilms();

        assertEquals(2, returnedFilmsList.size(), "size списка фильмов != 2");
    }

    @DisplayName("обновить пользователя")
    @Test
    public void updateFilm() {
        Film returnedFilm = filmDao.createFilm(defaultFilm1);
        returnedFilm.setName("new_name_1");
        Film returnedUpdatedFilm = filmDao.updateFilm(returnedFilm);

        Film returnedFilmById = filmDao.getById(returnedUpdatedFilm.getId());
        assertEquals("new_name_1", returnedFilmById.getName(), "name фильма != \"new_name_1\"");
    }

    @DisplayName("удалить пользователя по его id")
    @Test
    public void deleteFilm() throws SQLException {
        Film returnedFilm = filmDao.createFilm(defaultFilm1);
        filmDao.deleteFilm(returnedFilm.getId());

        List<Film> returnedFilmsList = filmDao.getAllFilms();
        assertEquals(0, returnedFilmsList.size(), "size списка фильмов != 0");
    }

    @DisplayName("выдать список популярных фильмов")
    @Test
    public void getPopular() throws SQLException {
        User returnedUser = userDao.createUser(defaultUser);
        Film returnedFilm1 = filmDao.createFilm(defaultFilm1);
        Film returnedFilm2 = filmDao.createFilm(defaultFilm2);
        likeDao.addLike(returnedFilm2.getId(), returnedUser.getId());

        List<Film> returnedPopularFilms = filmDao.getPopular(10);
        assertEquals(2, returnedPopularFilms.size(), "size списка популярных фильмов != 2");
    }
}