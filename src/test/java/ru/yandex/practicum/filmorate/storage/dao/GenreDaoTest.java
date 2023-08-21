package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("GenreDaoTest должен ")
class GenreDaoTest {
    private final GenreDao genreDao;
    private final FilmDao filmDao;

    @DisplayName("выдать жанр по id")
    @Test
    public void getById() {
        Genre returnedGenre = genreDao.getById(1);
        assertEquals(1, returnedGenre.getId(), "id жанра != 1");
        assertEquals("Комедия", returnedGenre.getName(), "name жанра не 'Комедия'");
    }

    @DisplayName("выдать список всех жанров")
    @Test
    public void getGenreList() {
        List<Genre> returnedGenreList = genreDao.getGenreList();
        assertEquals(6, returnedGenreList.size(), "size списка жанров != 6");
    }

    @DisplayName("выдать список жанров фильма по id фильма")
    @Test
    public void getGenresByFilmId() {
        Genre genre = Genre.builder().id(1).name("Комедия").build();
        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        Film returnedFilm = filmDao.createFilm(
                Film.builder()
                        .name("name")
                        .releaseDate(LocalDate.of(2010, 1, 1))
                        .duration(160)
                        .description("description")
                        .rate(1)
                        .mpa(Mpa.builder().id(1).name("G").build())
                        .genres(genres)
                        .build()
        );

        Set<Genre> returnedGenreSet = genreDao.getGenresByFilmId(returnedFilm.getId());
        assertEquals(1, returnedGenreSet.size(), "size списка жанров != 1");
    }

    @DisplayName("выдать исключение, при отрицательном id")
    @Test
    public void idValidationWithNegativeId() {
        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> genreDao.idValidation(-1)
        );

        assertEquals("🔹ваш id: -1 -- отрицательный либо равен 0", exception.getMessage());
    }

    @DisplayName("выдать исключение, при несуществующем id")
    @Test
    public void idValidationWithNonExistent() {
        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> genreDao.idValidation(9999)
        );

        assertEquals("🔹введен несуществующий id: 9999", exception.getMessage());
    }
}