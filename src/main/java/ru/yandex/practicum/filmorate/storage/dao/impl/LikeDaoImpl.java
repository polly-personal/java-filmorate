package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.LikeDao;

@Component
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlRequest = "INSERT INTO PUBLIC.\"like\" (film_id, user_id) VALUES (?, ?);";
        jdbcTemplate.update(sqlRequest, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlRequest = "DELETE FROM PUBLIC.\"like\" WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sqlRequest, filmId, userId);
    }
}
