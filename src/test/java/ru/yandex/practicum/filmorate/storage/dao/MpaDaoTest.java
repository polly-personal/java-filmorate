package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
}