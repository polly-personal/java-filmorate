package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
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
        String sqlRequest = "SELECT * FROM PUBLIC.\"mpa\" WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            Mpa mpa = Mpa.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .build();

            return mpa;
        }

        throw new IdNotFoundException("введен несуществующий id: " + id);
    }

    @Override
    public List<Mpa> getMpaList() throws SQLException {
        String sqlRequest = "SELECT * FROM PUBLIC.\"mpa\" ORDER BY id;";
        List<Mpa> mpas = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeMpa(resultSet));

        return mpas;
    }

    @Override
    public boolean idIsExists(long id) {
        String sqlRequest = "SELECT id FROM PUBLIC.\"mpa\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            return true;
        }
        return false;
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
