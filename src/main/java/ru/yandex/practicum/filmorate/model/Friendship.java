package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Friendship {
    @NotNull(message = "поле \"userId\" должно быть заполнено")
    private long userId;

    @NotNull(message = "поле \"friendId\" должно быть заполнено")
    private long friendId;

    private boolean isApproved;
}
