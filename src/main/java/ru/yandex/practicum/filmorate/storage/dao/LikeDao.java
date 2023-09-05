package ru.yandex.practicum.filmorate.storage.dao;

public interface LikeDao {
    void addLike(long id, long userId);

    void deleteLike(long id, long userId);
}
