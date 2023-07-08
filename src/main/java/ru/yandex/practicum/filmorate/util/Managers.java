package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.manager.*;

public class Managers {

    public static UsersManager getDefaultUsersManager() {
        return new UsersManager();
    }

    public static FilmsManager getDefaultFilmsManager() {
        return new FilmsManager();
    }
}
