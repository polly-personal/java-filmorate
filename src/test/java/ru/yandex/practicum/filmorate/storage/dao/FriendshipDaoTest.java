package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("FriendshipDaoTest должен ")
class FriendshipDaoTest {
    private final UserDao userDao;
    private final FriendshipDao friendshipDao;
    private User defaultUser;
    private User defaultFriend;

    @BeforeEach
    public void createDefaultUsers() {
        defaultUser = userDao.createUser(
                User.builder()
                        .email("email@mail.ru")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(2010, 1, 1))
                        .build()
        );

        defaultFriend = userDao.createUser(
                User.builder()
                        .email("friend_email@mail.ru")
                        .login("friend_login")
                        .name("friend_name")
                        .birthday(LocalDate.of(2011, 1, 1))
                        .build()
        );
    }

    @DisplayName("выдать список id друзей по id пользователя")
    @Test
    public void getFriendsIdsByUserId() throws SQLException {
        User returnedUser = defaultUser;
        User returnedFriend = defaultFriend;

        friendshipDao.sendFriendRequest(returnedUser.getId(), returnedFriend.getId());
        friendshipDao.approveFriendRequestForOneUserOnly(returnedUser.getId(), returnedFriend.getId());

        Set<Long> returnedFriendsIdsForUser = friendshipDao.getFriendsIdsByUserId(returnedUser.getId());
        assertEquals(1, returnedFriendsIdsForUser.size(), "size списка друзей != 1");
    }

    @DisplayName("добавить \"друга\" только для пользователя (у \"друга\" нет друга)")
    @Test
    public void sendFriendRequest() throws SQLException {
        User returnedUser = defaultUser;
        User returnedFriend = defaultFriend;

        friendshipDao.sendFriendRequest(returnedUser.getId(), returnedFriend.getId());
        friendshipDao.approveFriendRequestForOneUserOnly(returnedUser.getId(), returnedFriend.getId());

        Set<Long> returnedFriendsIdsForUser = friendshipDao.getFriendsIdsByUserId(returnedUser.getId());
        assertEquals(1, returnedFriendsIdsForUser.size(), "size списка друзей != 1");

        Set<Long> returnedFriendsIdsForFriend = friendshipDao.getFriendsIdsByUserId(returnedFriend.getId());
        assertEquals(0, returnedFriendsIdsForFriend.size(), "size списка друзей != 0");
    }
}