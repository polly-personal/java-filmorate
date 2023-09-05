package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film newFilm) {
        String sqlMpaRequest = "SELECT * FROM PUBLIC.\"mpa\" WHERE id = ?";
        SqlRowSet sqlRowSetMpa = jdbcTemplate.queryForRowSet(sqlMpaRequest, newFilm.getMpa().getId());

        if (sqlRowSetMpa.next()) {
            newFilm.setMpa(
                    Mpa.builder()
                            .id(sqlRowSetMpa.getLong("id"))
                            .name(sqlRowSetMpa.getString("name"))
                            .build()
            );
        }

        String sqlInsertRequest =
                "INSERT INTO PUBLIC.\"films\" (name, description, release_date, duration, rate, mpa_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlInsertRequest, new String[]{"id"});

                    preparedStatement.setString(1, newFilm.getName());
                    preparedStatement.setString(2, newFilm.getDescription());
                    preparedStatement.setDate(3, Date.valueOf(newFilm.getReleaseDate()));
                    preparedStatement.setDouble(4, newFilm.getDuration());
                    preparedStatement.setLong(5, newFilm.getRate());
                    preparedStatement.setLong(6, newFilm.getMpa().getId());

                    return preparedStatement;
                },
                keyHolder);
        long id = keyHolder.getKey().longValue();

        Set<Genre> onlyIdGenresFromJson = newFilm.getGenres();
        Set<Genre> newGenres = new HashSet<>();

        if (onlyIdGenresFromJson != null && !onlyIdGenresFromJson.isEmpty()) {

            String sqlGenresRequest = "SELECT * FROM PUBLIC.\"genres\" WHERE id = ?";
            for (Genre genreWithOnlyId : onlyIdGenresFromJson) {
                SqlRowSet sqlRowSetGenres = jdbcTemplate.queryForRowSet(sqlGenresRequest, genreWithOnlyId.getId());

                if (sqlRowSetGenres.next()) {
                    Genre genre = Genre.builder()
                            .id(sqlRowSetGenres.getLong("id"))
                            .name(sqlRowSetGenres.getString("name"))
                            .build();

                    newGenres.add(genre);
                    newFilm.setGenres(newGenres);
                }
            }
            addGenresById(id, newFilm.getGenres());
        } else {
            newFilm.setGenres(newGenres);
            addGenresById(id, newFilm.getGenres());
        }

        return getById(id);
    }

    @Override
    public Film getById(long id) throws IdNotFoundException {
        String sqlRequest = "SELECT " +
                "f.id film_id, " +
                "f.name film_name, " +
                "f.description film_description, " +
                "f.release_date film_release_date, " +
                "f.duration film_duration, " +
                "f.rate film_rate, " +
                "f.likes film_likes, " +

                "m.id AS mpa_id, " +
                "m.name AS mpa_name, " +
                "fg.genre_id AS genre_id, " +
                "g.name AS genre_name, " +
                "COUNT(l.user_id) AS film_likes " +

                "FROM PUBLIC.\"films\" AS f " +

                "LEFT JOIN PUBLIC.\"mpa\" AS m ON f.mpa_id = m.id " +
                "LEFT JOIN PUBLIC.\"film_genres\" AS fg ON f.id = fg.film_id " +
                "LEFT JOIN PUBLIC.\"genres\" AS g ON genre_id = g.id " +
                "LEFT JOIN PUBLIC.\"likes\" AS l ON f.id = l.film_id " +

                "WHERE f.id = ? " +
                "GROUP BY f.id, fg.genre_id; ";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        Map<Long, Film> idAndFilm = new HashMap<>();
        Set<Genre> currentGenres = new HashSet<>();

        while (sqlRowSet.next()) {

            if (!idAndFilm.containsKey(id)) {
                Mpa mpa = new Mpa(sqlRowSet.getLong("MPA_ID"), sqlRowSet.getString("MPA_NAME"));
                Film film = Film.builder()
                        .id(sqlRowSet.getLong("FILM_ID"))
                        .name(sqlRowSet.getString("FILM_NAME"))
                        .description(sqlRowSet.getString("FILM_DESCRIPTION"))
                        .releaseDate(sqlRowSet.getDate("RELEASE_DATE").toLocalDate())
                        .duration(sqlRowSet.getDouble("DURATION"))
                        .rate(sqlRowSet.getLong("RATE"))
                        .likes(sqlRowSet.getLong("FILM_LIKES"))
                        .mpa(mpa)
                        .build();
                idAndFilm.put(id, film);
            }

            Genre genre = new Genre(sqlRowSet.getLong("GENRE_ID"), sqlRowSet.getString("GENRE_NAME"));

            Film film = idAndFilm.get(id);
            Set<Genre> genresFromFilm = film.getGenres();

            if (genresFromFilm == null && genre.getId() != 0) {
                genresFromFilm = currentGenres;

                genresFromFilm.add(genre);
                film.setGenres(genresFromFilm);

            } else if (genresFromFilm != null && genre.getId() != 0) {
                genresFromFilm.add(genre);

            } else if (genresFromFilm == null && genre.getId() == 0) {
                film.setGenres(currentGenres);
            }
        }

        if (idAndFilm.containsKey(id)) {
            return idAndFilm.get(id);
        }
        throw new IdNotFoundException("введен несуществующий id: " + id);
    }

    @Override
    public List<Film> getAllFilms() throws SQLException {
        String sqlRequest = "SELECT " +
                "*, " +
                "m.id AS mpa_id, " +
                "m.name AS mpa_name, " +
                "fg.genre_id AS genre_id, " +
                "g.name AS genre_name, " +
                "COUNT(l.user_id) AS film_likes " +

                "FROM PUBLIC.\"films\" AS f " +

                "LEFT JOIN PUBLIC.\"mpa\" AS m ON f.mpa_id = m.id " +
                "LEFT JOIN PUBLIC.\"film_genres\" AS fg ON f.id = fg.film_id " +
                "LEFT JOIN PUBLIC.\"genres\" AS g ON genre_id = g.id " +
                "LEFT JOIN PUBLIC.\"likes\" AS l ON f.id = l.film_id " +

                "GROUP BY f.id; ";

        List<Film> filmsWithoutGenres = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeFilm(resultSet));
        return filmsWithoutGenres;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        long id = updatedFilm.getId();

        String sqlRequest = "UPDATE PUBLIC.\"films\" SET " +
                "name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "duration = ?, " +
                "mpa_id = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(
                sqlRequest,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getId()
        );

        deleteGenresById(id);
        addGenresById(id, updatedFilm.getGenres());

        return getById(updatedFilm.getId());
    }

    @Override
    public String deleteFilm(long id) {
        String sqlRequest = "DELETE FROM PUBLIC.\"films\" WHERE id = ?;";
        jdbcTemplate.update(sqlRequest, id);

        return "фильм с id: " + id + " удален";
    }

    @Override
    public List<Film> getPopular(int count) throws SQLException {
        String request = "SELECT " +
                "*, " +
                "m.id AS mpa_id, " +
                "m.name AS mpa_name, " +
                "fg.genre_id AS genre_id, " +
                "g.name AS genre_name, " +
                "COUNT(l.user_id) AS film_likes " +

                "FROM PUBLIC.\"films\" AS f " +

                "LEFT JOIN PUBLIC.\"mpa\" AS m ON f.mpa_id = m.id " +
                "LEFT JOIN PUBLIC.\"film_genres\" AS fg ON f.id = fg.film_id " +
                "LEFT JOIN PUBLIC.\"genres\" AS g ON genre_id = g.id " +
                "LEFT JOIN PUBLIC.\"likes\" AS l ON f.id = l.film_id " +

                "GROUP BY f.id " +
                "ORDER BY film_likes DESC " +
                "LIMIT ?;";
        List<Film> films = jdbcTemplate.query(request, (resultSet, rowNum) -> makeFilm(resultSet), count);

        return films;
    }

    @Override
    public boolean idIsExists(long id) {
        String sqlRequest = "SELECT id FROM PUBLIC.\"films\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            return true;
        }
        return false;
    }


    private void addGenresById(long id, Set<Genre> genres) {
        String sqlRequest = "INSERT INTO PUBLIC.\"film_genres\" (film_id, genre_id) " +
                "VALUES (?, ?);";
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlRequest, id, genre.getId());
            }
        }
    }

    private void deleteGenresById(long id) {
        String sqlRequest = "DELETE FROM PUBLIC.\"film_genres\" WHERE film_id = ?;";
        jdbcTemplate.update(sqlRequest, id);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        Mpa mpa = new Mpa(resultSet.getLong("mpa_id"), resultSet.getString("mpa_name"));

        Set<Genre> genres = new HashSet<>();
        Genre genre = new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre_name"));
        if (genre.getId() != 0) {
            genres.add(genre);
        }

        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getDouble("duration"))
                .rate(resultSet.getLong("rate"))
                .likes(resultSet.getLong("film_likes"))
                .mpa(mpa)
                .genres(genres)
                .build();
        return film;
    }
}
