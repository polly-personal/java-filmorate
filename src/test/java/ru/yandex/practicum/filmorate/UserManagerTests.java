package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.manager.UsersManager;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Managers;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UserManagerTests должен ")
//@SpringBootTest
class UserManagerTests {
    UsersManager usersManager;
    HashMap<Integer, User> users;
//	int currentID;

    @BeforeEach
    public void createUserManager() {
        usersManager = Managers.getDefaultUsersManager();
        users = usersManager.getUsers();
//		usersManager.getCurrentID();
//		currentID = usersManager.getCurrentID();
    }

    @AfterEach
    public void clearUserManager() {
        users.clear();
//        System.out.println("usersManager.getUsers(): " + usersManager.getUsers());
//		currentID = 0;
        usersManager.setCurrentID(0);
//        System.out.println("usersManager.getCurrentID(): " + usersManager.getCurrentID());
    }

    @DisplayName("создать пользователя")
    @Test
    void createUser() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        User createdUser = usersManager.createUser(user);

        assertEquals(1, createdUser.getId(), "ID созданного пользователя != 1");
        assertEquals("some@yandex.ru", createdUser.getEmail());
        assertEquals(1, users.size(), "размер мапы != 1");
    }

    @DisplayName("НЕ создавать пользователя, если некорректная email")
    @Test
    void doNotCreateUserWithIncorrectEmail() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .id(0)
                .email("someyandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usersManager.createUser(user)
        );

        assertEquals("🔹некорректная email", exception.getMessage());
        assertEquals(0, users.size(), "размер мапы != 0");
    }

    @DisplayName("НЕ создавать пользователя, если некорректный login")
    @Test
    void doNotCreateUserWithIncorrectLogin() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .id(0)
                .email("some@yandex.ru")
                .login("user Login")
                .name("userName")
                .birthday(birthday)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usersManager.createUser(user)
        );

        assertEquals("🔹некорректный login", exception.getMessage());
        assertEquals(0, users.size(), "размер мапы != 0");
    }
    @DisplayName("создать пользователя, если name=null")
    @Test
    void сreateUserWithNullName() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .birthday(birthday)
                .build();
        User createdUser = usersManager.createUser(user);

        assertEquals(1, createdUser.getId(), "ID созданного пользователя != 1");
        assertEquals("userLogin", createdUser.getName());
        assertEquals(1, users.size(), "размер мапы != 1");
    }

    @DisplayName("НЕ создавать пользователя, если birthday в будущем")
    @Test
    void doNotCreateUserWithIncorrectBirthday() {
        LocalDate birthday = LocalDate.of(3021, 6, 7);
        User user = User.builder()
                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> usersManager.createUser(user)
        );

        assertEquals("🔹birthday не может быть в будущем", exception.getMessage());
        assertEquals(0, users.size(), "размер мапы != 0");
    }

}
