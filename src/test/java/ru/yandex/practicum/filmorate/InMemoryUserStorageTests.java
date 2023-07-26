package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.util.Managers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("InMemoryUserStorageTests должен ")
class InMemoryUserStorageTests {
    InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    public void createInMemoryUserStorage() {
        inMemoryUserStorage = Managers.getDefaultInMemoryUserStorage();
    }

    @AfterEach
    public void clearUserManager() {
        inMemoryUserStorage.getUsersList().clear();
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
        User createdUser = inMemoryUserStorage.createUser(user);

        assertEquals("some@yandex.ru", createdUser.getEmail());
        assertEquals(1, inMemoryUserStorage.getUsersList().size(), "размер мапы != 1");
    }

    @DisplayName("НЕ создавать пользователя, если email уже существует")
    @Test
    void doNotCreateUserWithIncorrectEmail() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        inMemoryUserStorage.createUser(user);

        User user2 = User.builder()
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();

        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> inMemoryUserStorage.createUser(user2)
        );

        assertEquals("🔹пользователь с таким email уже существует", exception.getMessage());
        assertEquals(1, inMemoryUserStorage.getUsersList().size(), "размер мапы != 1");
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
        User createdUser = inMemoryUserStorage.createUser(user);

        assertEquals("userLogin", createdUser.getName());
        assertEquals(1, inMemoryUserStorage.getUsersList().size(), "размер мапы != 1");
    }

    @DisplayName("выдавать пользователя по его id")
    @Test
    void getById() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        inMemoryUserStorage.createUser(user);
        User givenUser = inMemoryUserStorage.getById(user.getId());

        assertEquals("some@yandex.ru", givenUser.getEmail());
        assertEquals(1, inMemoryUserStorage.getUsersList().size(), "размер мапы != 1");
    }

    @DisplayName("обновлять пользователя")
    @Test
    void updateUser() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        inMemoryUserStorage.createUser(user);

        User update = User.builder()
                .id(1)
                .email("2some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();

        User updatedUser = inMemoryUserStorage.updateUser(update);

        assertEquals("2some@yandex.ru", updatedUser.getEmail());
        assertEquals(1, inMemoryUserStorage.getUsersList().size(), "размер мапы != 1");
    }

    @DisplayName("удалить пользователя")
    @Test
    void deleteUser() {
        LocalDate birthday = LocalDate.of(2021, 6, 7);
        User user = User.builder()
                .email("some@yandex.ru")
                .login("userLogin")
                .name("userName")
                .birthday(birthday)
                .build();
        User createdUser = inMemoryUserStorage.createUser(user);

        inMemoryUserStorage.deleteUser(createdUser.getId());

        assertEquals(0, inMemoryUserStorage.getUsersList().size(), "размер мапы != 0");
    }
}
