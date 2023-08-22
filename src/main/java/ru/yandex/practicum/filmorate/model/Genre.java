package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class Genre {
    @NotNull(message = "поле \"id\" должно быть заполнено")
    private long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено")
    private String name;
}
