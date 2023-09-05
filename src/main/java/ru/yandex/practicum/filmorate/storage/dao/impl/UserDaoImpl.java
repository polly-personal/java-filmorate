package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User createUser(User newUser) {
        String sqlRequest = "INSERT INTO PUBLIC.\"users\" (email, login, name, birthday) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest, new String[]{"id"});

                    preparedStatement.setString(1, newUser.getEmail());
                    preparedStatement.setString(2, newUser.getLogin());
                    preparedStatement.setString(3, newUser.getName());
                    preparedStatement.setDate(4, Date.valueOf(newUser.getBirthday()));

                    return preparedStatement;
                },
                keyHolder);

        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public User getById(long id) throws IdNotFoundException {
        String sqlRequest = "SELECT " +
                "u.id AS user_id, " +
                "u.email AS user_email, " +
                "u.login AS user_login, " +
                "u.name AS user_name, " +
                "u.birthday AS user_birthday, " +
                "fsh.friend_id AS friend_id " +

                "FROM PUBLIC.\"users\" AS u " +

                "LEFT JOIN PUBLIC.\"friendships\" AS fsh ON u.id = fsh.user_id " +

                "WHERE u.id = ? " +
                "GROUP BY u.id, fsh.friend_id; ";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        Map<Long, User> idAndUser = new HashMap<>();
        Set<Long> currentIdFriends = new HashSet<>();

        while (sqlRowSet.next()) {

            if (!idAndUser.containsKey(id)) {
                User user = User.builder()
                        .id(sqlRowSet.getLong("USER_ID"))
                        .email(sqlRowSet.getString("USER_EMAIL"))
                        .name(sqlRowSet.getString("USER_NAME"))
                        .login(sqlRowSet.getString("USER_LOGIN"))
                        .birthday(sqlRowSet.getDate("USER_BIRTHDAY").toLocalDate())
                        .build();
                idAndUser.put(id, user);
            }

            Long friendId = sqlRowSet.getLong("FRIEND_ID");

            User user = idAndUser.get(id);
            Set<Long> friendsIdsFromUser = user.getFriendsIds();

            if (friendsIdsFromUser == null && friendId != 0) {
                friendsIdsFromUser = currentIdFriends;

                friendsIdsFromUser.add(friendId);
                user.setFriendsIds(friendsIdsFromUser);

            } else if (friendsIdsFromUser != null && friendId != 0) {
                friendsIdsFromUser.add(friendId);

            } else if (friendsIdsFromUser == null && friendId == 0) {
                user.setFriendsIds(currentIdFriends);
            }
        }

        if (idAndUser.containsKey(id)) {
            return idAndUser.get(id);
        }
        throw new IdNotFoundException("введен несуществующий id: " + id);
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        String sqlRequest = "SELECT * FROM PUBLIC.\"users\";";
        List<User> users = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeUser(resultSet));

        return users;
    }

    @Override
    public User updateUser(User updatedUser) {
        String sqlUpdateRequest = "UPDATE PUBLIC.\"users\" SET " +
                "email = ?, " +
                "login = ?, " +
                "name = ?, " +
                "birthday = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(
                sqlUpdateRequest,
                updatedUser.getEmail(),
                updatedUser.getLogin(),
                updatedUser.getName(),
                updatedUser.getBirthday(),
                updatedUser.getId());

        return getById(updatedUser.getId());
    }

    @Override
    public String deleteUser(long id) {
        String sqlRequest = "DELETE FROM PUBLIC.\"users\" WHERE id = ?";
        jdbcTemplate.update(sqlRequest, id);

        return "пользователь с id: " + id + " удален";
    }

    @Override
    public boolean idIsExists(long id) {
        String sqlRequest = "SELECT id FROM PUBLIC.\"users\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean emailIsExists(String email) {
        String sqlRequest = "SELECT * FROM PUBLIC.\"users\" WHERE email = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, email);

        if (sqlRowSet.next()) {
            return false;
        }
        return true;
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate()).build();
    }
}
