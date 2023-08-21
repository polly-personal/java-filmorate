package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;
    private final UserDao userDao;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, MpaDao mpaDao, GenreDao genreDao, LikeDao likeDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.likeDao = likeDao;
        this.userDao = userDao;
    }

    @Override
    public Film createFilm(Film newFilm) throws ValidationException {
        filmValidation(newFilm);

        Mpa mpaById = mpaDao.getById(newFilm.getMpa().getId());
        newFilm.setMpa(mpaById);

        String sqlRequest =
                "INSERT INTO PUBLIC.\"film\" (name, description, release_date, duration, rate, mpa_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest, new String[]{"id"});

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

        Set<Genre> genresFromClient = newFilm.getGenres();
        Set<Genre> newGenres = new HashSet<>();

        if (genresFromClient != null && !genresFromClient.isEmpty()) {

            for (Genre genre : genresFromClient) {
                Genre genreById = genreDao.getById(genre.getId());
                newGenres.add(genreById);
            }
            newFilm.setGenres(newGenres);
            addGenresById(id, newFilm.getGenres());
        }

        return getById(id);
    }

    @Override
    public Film getById(long id) throws ValidationException {
        idValidation(id);

        String sqlRequest = "SELECT * FROM PUBLIC.\"film\" WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            Film film = Film.builder()
                    .id(sqlRowSet.getLong("id"))
                    .name(sqlRowSet.getString("name"))
                    .description(sqlRowSet.getString("description"))
                    .releaseDate(sqlRowSet.getDate("release_date").toLocalDate())
                    .duration(sqlRowSet.getDouble("duration"))
                    .rate(sqlRowSet.getLong("rate"))
                    .genres(genreDao.getGenresByFilmId(id))
                    .mpa(mpaDao.getById(sqlRowSet.getLong("mpa_id")))
                    .build();

            return film;
        }

        return null;
    }

    @Override
    public List<Film> getFilmsList() {
        String sqlRequest = "SELECT * FROM PUBLIC.\"film\"";
        List<Film> films = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeFilm(resultSet));

        return films;
    }

    @Override
    public Film updateFilm(Film updatedFilm) throws ValidationException {
        long id = updatedFilm.getId();
        idValidation(id);
        filmValidation(updatedFilm);

        String sqlRequest = "UPDATE PUBLIC.\"film\" SET " +
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
    public String deleteFilm(long id) throws ValidationException {
        String sqlRequest = "DELETE FROM PUBLIC.\"film\" WHERE id = ?;";
        jdbcTemplate.update(sqlRequest, id);

        return "фильм с id: " + id + " удален";
    }

    @Override
    public Film addLike(long id, long userId) {
        idValidation(id);
        userDao.idValidation(userId);

        likeDao.addLike(id, userId);
        return getById(id);
    }

    @Override
    public Film deleteLike(long id, long userId) {
        idValidation(id);
        userDao.idValidation(userId);

        likeDao.deleteLike(id, userId);
        return getById(id);
    }

    @Override
    public List<Film> getPopular(int count) {
        countForPopularValidation(count);

        String request = "SELECT *, " +
                "COUNT(l.user_id) AS film_likes " +
                "FROM PUBLIC.\"film\" AS f " +
                "LEFT JOIN  PUBLIC.\"like\" AS l  ON f.id = l.film_id " +
                "GROUP BY f.id " +
                "ORDER BY film_likes DESC " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(request, (resultSet, rowNum) -> makeFilm(resultSet), count);

        return films;
    }

    @Override
    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        String sqlRequest = "SELECT id FROM PUBLIC.\"film\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (!sqlRowSet.next()) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }

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
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getDouble("duration"))
                .rate(resultSet.getLong("rate"))
                .genres(genreDao.getGenresByFilmId(resultSet.getLong("id")))
                .mpa(mpaDao.getById(resultSet.getLong("mpa_id")))
                .build();
    }

    private void filmValidation(Film film) {
        descriptionValidation(film.getDescription());
        releaseDateValidation(film.getReleaseDate());
        durationValidation(film.getDuration());
    }

    private void descriptionValidation(String description) throws ValidationException {
        if (description != null) {
            if (description.isBlank()) {
                throw new ValidationException("некорректный description");
            }
        }
    }

    private void releaseDateValidation(LocalDate releaseDate) throws ValidationException {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("\"releaseDate\" не может быть раньше, чем 1895/12/28");
        }
    }

    private void durationValidation(double duration) throws ValidationException {
        if (duration < 0.1) {
            throw new ValidationException("поле \"duration\" не может быть отрицательным или равно нулю");
        }
    }

    private void countForPopularValidation(int count) {
        if (count <= 0) {
            throw new ValidationException("некорректный параметр запроса -- count: " + count);
        }
    }
}