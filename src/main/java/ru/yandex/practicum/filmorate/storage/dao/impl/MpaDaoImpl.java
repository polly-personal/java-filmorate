package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getById(long id) {
        idValidation(id);

        String sqlRequest = "SELECT * FROM PUBLIC.\"mpa\" WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            Mpa mpa = Mpa.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .build();

            return mpa;
        }

        return null;
    }

    @Override
    public List<Mpa> getMpaList() {
        String sqlRequest = "SELECT * FROM PUBLIC.\"mpa\" ORDER BY id;";
        List<Mpa> mpas = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeMpa(resultSet));

        return mpas;
    }

    @Override
    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        String sqlRequest = "SELECT id FROM PUBLIC.\"mpa\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (!sqlRowSet.next()) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
