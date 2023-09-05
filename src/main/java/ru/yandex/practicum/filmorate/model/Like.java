package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Like {
    @NotNull(message = "поле \"filmId\" должно быть заполнено")
    private long filmId;

    @NotNull(message = "поле \"userId\" должно быть заполнено")
    private long userId;
}
