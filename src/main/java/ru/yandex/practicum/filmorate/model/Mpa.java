package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Mpa {
    private long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено")
    private String name;
}
