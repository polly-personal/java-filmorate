package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {

    @NotBlank(message = "поле \"name\" должно быть заполнено")
    private String name;

    private long id;

    @Size(max = 200, message = "длина поля \"description\" не должна привышать 200 символов")
    private String description;

    @NotNull(message = "поле \"releaseDate\" должно быть заполнено")
    private LocalDate releaseDate;

    private double duration;

    @JsonIgnore
    private Set<Long> likes;
}
