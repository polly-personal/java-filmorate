package ru.yandex.practicum.filmorate.exception;

public class FriendsListNotFoundException extends RuntimeException {
    public FriendsListNotFoundException(String message) {
        super("🔹" + message);
    }
}
