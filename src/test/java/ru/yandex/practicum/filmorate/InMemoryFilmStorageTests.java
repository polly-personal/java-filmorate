package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.util.Managers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("InMemoryFilmStorageTests должен ")
public class InMemoryFilmStorageTests {
    InMemoryFilmStorage inMemoryFilmStorage;

    @BeforeEach
    public void createUserManager() {
        inMemoryFilmStorage = Managers.getDefaultInMemoryFilmStorage();
    }

    @AfterEach
    public void clearFilmManager() {
        inMemoryFilmStorage.getFilmsList().clear();
    }

    @DisplayName("создать фильм")
    @Test
    void createFilm() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();
        Film createdFilm = inMemoryFilmStorage.createFilm(film);

        assertEquals(1, createdFilm.getId(), "ID созданного фильма != 1");
        assertEquals("filmName", createdFilm.getName());
        assertEquals(1, inMemoryFilmStorage.getFilmsList().size(), "размер мапы != 1");
    }

    @DisplayName("НЕ создавать фильм, если duration<0")
    @Test
    void doNotCreateFilmWithIncorrectDuration() {
        LocalDate releaseDate = LocalDate.of(1984, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(-1)
                .build();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.createFilm(film)
        );

        assertEquals("🔹поле \"duration\" не может быть отрицательным или равно нулю", exception.getMessage());
        assertEquals(0, inMemoryFilmStorage.getFilmsList().size(), "размер мапы != 0");
    }

    @DisplayName("выдавать фильм по id")
    @Test
    void getById() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();
        Film createdFilm = inMemoryFilmStorage.createFilm(film);

        Film filmById = inMemoryFilmStorage.getById(createdFilm.getId());

        assertEquals("filmName", filmById.getName());
        assertEquals(1, inMemoryFilmStorage.getFilmsList().size(), "размер мапы != 1");
    }

    @DisplayName("обновлять фильм")
    @Test
    void updateFilm() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();
        Film createdFilm = inMemoryFilmStorage.createFilm(film);

        LocalDate releaseDate2 = LocalDate.of(2021, 6, 7);
        Film update = Film.builder()
                .id(1)
                .name("2filmName")
                .description("some")
                .releaseDate(releaseDate2)
                .duration(120)
                .build();

        Film updatedFilm = inMemoryFilmStorage.updateFilm(update);

        assertEquals(1, updatedFilm.getId(), "ID созданного фильма != 1");
        assertEquals("2filmName", updatedFilm.getName());
        assertEquals(1, inMemoryFilmStorage.getFilmsList().size(), "размер мапы != 1");
    }

    @DisplayName("удалять фильм")
    @Test
    void deleteFilm() {
        LocalDate releaseDate = LocalDate.of(2021, 6, 7);
        Film film = Film.builder()
                .id(0)
                .name("filmName")
                .description("some")
                .releaseDate(releaseDate)
                .duration(120)
                .build();
        Film createdFilm = inMemoryFilmStorage.createFilm(film);

        inMemoryFilmStorage.deleteFilm(createdFilm.getId());

        assertEquals(0, inMemoryFilmStorage.getFilmsList().size(), "размер мапы != 0");
    }
}
