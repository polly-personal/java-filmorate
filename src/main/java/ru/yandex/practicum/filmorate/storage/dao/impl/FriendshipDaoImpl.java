package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Set<Long> getFriendsIdsByUserId(long id) {
        String sqlRequest =
                "SELECT friend_id FROM PUBLIC.\"friendship\" " +
                        "WHERE user_id = ? " +
                        "AND is_approved = true;";
        List<Long> friendIds = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeFriendId(resultSet), id);

        return new HashSet<>(friendIds);
    }

    @Override
    public void sendFriendRequest(long userId, long friendId) {
        String sqlRequest =
                "INSERT INTO PUBLIC.\"friendship\" (user_id, friend_id, is_approved) " +
                        "VALUES (?, ?, false);";
        jdbcTemplate.update(sqlRequest, userId, friendId);
        jdbcTemplate.update(sqlRequest, friendId, userId);
    }

    @Override
    public void approveFriendRequestForOneUserOnly(long userId, long friendId) {
        String sqlRequest =
                "UPDATE PUBLIC.\"friendship\" SET " +
                        "is_approved = true " +
                        "WHERE user_id = ?" +
                        "AND friend_id = ?;";
        jdbcTemplate.update(sqlRequest, userId, friendId);
    }

    @Override
    public void approveFriendRequestForBothUsers(long userId, long friendId) {
        String sqlRequest =
                "UPDATE PUBLIC.\"friendship\" SET " +
                        "is_approved = true " +
                        "WHERE user_id = ?" +
                        "AND friend_id = ?;";
        jdbcTemplate.update(sqlRequest, userId, friendId);
        jdbcTemplate.update(sqlRequest, friendId, userId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sqlRequest =
                "UPDATE PUBLIC.\"friendship\" SET " +
                        "is_approved = false " +
                        "WHERE user_id = ?" +
                        "AND friend_id = ?;";
        jdbcTemplate.update(sqlRequest, userId, friendId);
        jdbcTemplate.update(sqlRequest, friendId, userId);
    }

    private long makeFriendId(ResultSet resultSet) throws SQLException {
        return resultSet.getLong("friend_id");
    }
}
