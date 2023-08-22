package ru.yandex.practicum.filmorate.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendsListNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.dao.UserDao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final FriendshipDao friendshipDao;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, FriendshipDao friendshipDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendshipDao = friendshipDao;
    }

    @Override
    public User createUser(User newUser) throws ValidationException {
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
    public User getById(long id) throws ValidationException, IdNotFoundException {
        String sqlRequest = "SELECT * FROM PUBLIC.\"users\" WHERE id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (sqlRowSet.next()) {
            User user = User.builder()
                    .id(sqlRowSet.getLong("id"))
                    .email(sqlRowSet.getString("email"))
                    .login(sqlRowSet.getString("login"))
                    .name(sqlRowSet.getString("name"))
                    .birthday(sqlRowSet.getDate("birthday").toLocalDate())
                    .friendsIds(friendshipDao.getFriendsIdsByUserId(id))
                    .build();
            return user;
        }
        throw new IdNotFoundException("введен несуществующий id: " + id);
    }

    @Override
    public List<User> getAllUsers() {
        String sqlRequest = "SELECT * FROM PUBLIC.\"users\";";
        List<User> users = jdbcTemplate.query(sqlRequest, (resultSet, rowNum) -> makeUser(resultSet));

        return users;
    }

    @Override
    public User updateUser(User updatedUser) throws IdNotFoundException, ValidationException {
        long id = updatedUser.getId();
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
    public String deleteUser(long id) throws IdNotFoundException, ValidationException {
        String sqlRequest = "DELETE FROM PUBLIC.\"users\" WHERE id = ?";
        jdbcTemplate.update(sqlRequest, id);

        return "пользователь с id: " + id + " удален";
    }

    @Override
    public User addFriend(long id, long friendId) throws ValidationException, IdNotFoundException {
        friendshipDao.sendFriendRequest(id, friendId);
        friendshipDao.approveFriendRequestForOneUserOnly(id, friendId);

        return getById(id);
    }

    @Override
    public User deleteFriend(long id, long friendId) throws IdNotFoundException, ValidationException,
            FriendsListNotFoundException {
        friendshipDao.deleteFriend(id, friendId);
        return getById(id);
    }

    @Override
    public Set<Long> getFriends(long id) throws ValidationException, IdNotFoundException,
            FriendsListNotFoundException {
        return friendshipDao.getFriendsIdsByUserId(id);
    }

    @Override
    public boolean emailIsValid(String email) throws ValidationException {
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

    @Override
    public void idValidation(long id) throws ValidationException, IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        String sqlRequest = "SELECT id FROM PUBLIC.\"users\" WHERE id = ?;";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlRequest, id);

        if (!sqlRowSet.next()) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }
    }
}
