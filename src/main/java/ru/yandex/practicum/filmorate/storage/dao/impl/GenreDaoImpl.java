package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(long id) {
        idValidation(id);

        String sqlRequest = "SELECT * FROM PUBLIC.\"genre\" WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            Genre genre = Genre.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .build();

            return genre;
        }

        return null;
    }

    @Override
    public List<Genre> getGenreList() {
        String sqlRequest = "SELECT * FROM PUBLIC.\"genre\" ORDER BY id;";
        List<Genre> genres = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeGenre(resultSet));

        return genres;
    }

    @Override
    public Set<Genre> getGenresByFilmId(long id) {
        String sqlRequest =
                "SELECT g.id, g.name " +
                        "FROM PUBLIC.\"film\" AS  f " +

                        "JOIN PUBLIC.\"film_genres\" AS fg ON f.id = fg.film_id " +
                        "JOIN PUBLIC.\"genre\" AS g ON  fg.genre_id = g.id " +

                        "WHERE f.id = ? " +
                        "ORDER BY g.id;";
        List<Genre> genres = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeGenre(resultSet), id);

        return new HashSet<>(genres);
    }


    @Override
    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        String sqlRequest = "SELECT id FROM PUBLIC.\"genre\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (!sqlRowSet.next()) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
