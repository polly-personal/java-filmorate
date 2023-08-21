package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("MpaDaoTest должен ")
class MpaDaoTest {
    private final MpaDao mpaDao;

    @DisplayName("выдать mpa-рейтинг по id")
    @Test
    public void getById() {
        Mpa returnedMpa = mpaDao.getById(1);
        assertEquals(1, returnedMpa.getId(), "id mpa-рейтинга != 1");
        assertEquals("G", returnedMpa.getName(), "name mpa-рейтинга не 'G'");
    }

    @DisplayName("выдать список всех mpa-рейтингов")
    @Test
    public void getMpaList() {
        List<Mpa> returnedMpaList = mpaDao.getMpaList();
        assertEquals(5, returnedMpaList.size(), "size списка mpa-рейтингов != 5");
    }

    @DisplayName("выдать исключение, при отрицательном id")
    @Test
    public void idValidationWithNegativeId() {
        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> mpaDao.idValidation(-1)
        );

        assertEquals("🔹ваш id: -1 -- отрицательный либо равен 0", exception.getMessage());
    }

    @DisplayName("выдать исключение, при несуществующем id")
    @Test
    public void idValidationWithNonExistent() {
        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> mpaDao.idValidation(9999)
        );

        assertEquals("🔹введен несуществующий id: 9999", exception.getMessage());
    }
}