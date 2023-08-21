package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class Film {

    private long id;

    @NotBlank(message = "поле \"name\" должно быть заполнено")
    private String name;

    @Size(max = 200, message = "длина поля \"description\" не должна привышать 200 символов")
    private String description;

    @NotNull(message = "поле \"releaseDate\" должно быть заполнено")
    private LocalDate releaseDate;

    private double duration;

    private long rate;

    private long likes;

    private Set<Genre> genres;

    @NotNull(message = "поле \"mpa\" должно быть заполнено")
    private Mpa mpa;
}
