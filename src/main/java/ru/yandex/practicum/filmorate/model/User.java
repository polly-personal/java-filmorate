package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private long id;

    @NotBlank(message = "поле \"email\" должно быть заполнено")
    @Email(message = "некорректный email")
    private String email;

    @NotBlank(message = "некорректный login")
    private String login;

    private String name;

    @NotNull(message = "поле \"birthday\" должно быть заполнено")
    @PastOrPresent(message = "поле \"birthday\" не может быть в будущем")
    private LocalDate birthday;

    @JsonIgnore
    private Set<Long> friendIds;
    @JsonIgnore
    private Set<Long> likedFilms;
}
