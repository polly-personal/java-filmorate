package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"/schema.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/delete_schema.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("UserDaoTest должен ")
class UserDaoTest {
    private final UserDao userDao;
    private User defaultUser;
    private User defaultFriend;

    @BeforeEach
    public void createDefaultUsers() {
        defaultUser = User.builder()
                .email("email@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2010, 1, 1))
                .build();

        defaultFriend = User.builder()
                .email("friend_email@mail.ru")
                .login("friend_login")
                .name("friend_name")
                .birthday(LocalDate.of(2011, 1, 1))
                .build();
    }

    @DisplayName("создать пользователя и выдать его по его id")
    @Test
    public void createUserAndGetById() {
        User returnedUser = userDao.createUser(defaultUser);

        User returnedUserById = userDao.getById(returnedUser.getId());
        assertEquals(1, returnedUserById.getId(), "id пользователя != 1");
        assertEquals("email@mail.ru", returnedUserById.getEmail(), "email пользователя != email@mail.ru");
    }

    @DisplayName("выдать список пользователей")
    @Test
    public void getUsersList() {
        userDao.createUser(defaultUser);
        userDao.createUser(defaultFriend);

        List<User> returnedUsersList = userDao.getAllUsers();
        assertEquals(2, returnedUsersList.size(), "size списка пользователей != 2");
    }

    @DisplayName("обновить пользователя")
    @Test
    public void updateUser() {
        User returnedUser = userDao.createUser(defaultUser);
        returnedUser.setEmail("updated_email@email.ru");
        User returnedUpdatedUser = userDao.updateUser(returnedUser);

        User returnedUserById = userDao.getById(returnedUpdatedUser.getId());
        assertEquals("updated_email@email.ru", returnedUserById.getEmail(), "email пользователя != \"updated_email@email.ru\"");
    }

    @DisplayName("удалить пользователя по его id")
    @Test
    public void deleteUser() {
        User returnedUser = userDao.createUser(defaultUser);
        userDao.deleteUser(returnedUser.getId());

        List<User> returnedUsersList = userDao.getAllUsers();
        assertEquals(0, returnedUsersList.size(), "size списка пользователей != 0");
    }

    @DisplayName("добавить пользователю друга, (у \"друга\" нет друга)")
    @Test
    public void addFriend() {
        User returnedUser = userDao.createUser(defaultUser);
        User returnedFriend = userDao.createUser(defaultFriend);
        userDao.addFriend(returnedUser.getId(), returnedFriend.getId());

        User returnedUserById = userDao.getById(returnedUser.getId());
        assertEquals(1, returnedUserById.getFriendsIds().size(), "size списка друзей != 1");

        User returnedFriendById = userDao.getById(returnedFriend.getId());
        assertEquals(0, returnedFriendById.getFriendsIds().size(), "size списка друзей != 0");
    }

    @DisplayName("удалить пользователю друга")
    @Test
    public void deleteFriend() {
        User returnedUser = userDao.createUser(defaultUser);
        User returnedFriend = userDao.createUser(defaultFriend);
        userDao.addFriend(returnedUser.getId(), returnedFriend.getId());
        userDao.deleteFriend(returnedUser.getId(), returnedFriend.getId());

        User returnedUserById = userDao.getById(returnedUser.getId());
        assertEquals(0, returnedUserById.getFriendsIds().size(), "size списка друзей != 0");

        User returnedFriendById = userDao.getById(returnedFriend.getId());
        assertEquals(0, returnedFriendById.getFriendsIds().size(), "size списка друзей != 0");
    }

    @DisplayName("выдать список id друзей по id пользователя")
    @Test
    public void getFriends() {
        User returnedUser = userDao.createUser(defaultUser);
        User returnedFriend = userDao.createUser(defaultFriend);
        userDao.addFriend(returnedUser.getId(), returnedFriend.getId());

        User returnedUserById = userDao.getById(returnedUser.getId());
        assertEquals(1, returnedUserById.getFriendsIds().size(), "size списка друзей != 1");
    }

    @DisplayName("выдать исключение, при отрицательном id")
    @Test
    public void idValidationWithNegativeId() {
        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> userDao.idValidation(-1)
        );

        assertEquals("🔹ваш id: -1 -- отрицательный либо равен 0", exception.getMessage());
    }

    @DisplayName("выдать исключение, при несуществующем id")
    @Test
    public void idValidationWithNonExistent() {
        IdNotFoundException exception = assertThrows(
                IdNotFoundException.class,
                () -> userDao.idValidation(9999)
        );

        assertEquals("🔹введен несуществующий id: 9999", exception.getMessage());
    }
}