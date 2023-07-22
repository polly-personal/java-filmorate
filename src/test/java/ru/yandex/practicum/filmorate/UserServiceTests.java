package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Managers;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("UserServiceTests должен ")
class UserServiceTests {
    UserService userService;
    Map<Long, User> users;

    @BeforeEach
    public void createUserManager() {
        userService = Managers.getDefaultUsersService();
        users = userService.getUsers();
    }

    @AfterEach
    public void clearUserManager() {
        users.clear();
        userService.setCurrentID(0);
    }

    @DisplayName("создать пользователя")
    @Test
    void createUser() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        User createdUser = userService.createUser(user);

        assertEquals("some@yandex.ru", createdUser.getEmail());
        assertEquals(1, users.size(), "размер мапы != 1");
    }

    @DisplayName("НЕ создавать пользователя, если некорректный email")
    @Test
    void doNotCreateUserWithIncorrectEmail() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("someyandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(user)
        );

        assertEquals("🔹некорректный email. ваш email: someyandex.ru", exception.getMessage());
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
                () -> userService.createUser(user)
        );

        assertEquals("🔹некорректный login", exception.getMessage());
        assertEquals(0, users.size(), "размер мапы != 0");
    }

    @DisplayName("создать пользователя, если name=null")
    @Test
    void createUserWithIncorrectName() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .id(0)
                .email("some@yandex.ru")
                .login("userLogin")
                .birthday(birthday)
                .build();
        User createdUser = userService.createUser(user);

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
                () -> userService.createUser(user)
        );

        assertEquals("🔹birthday не может быть в будущем", exception.getMessage());
        assertEquals(0, users.size(), "размер мапы != 0");
    }
}
