package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private String email;
    private final int id;
    private String login;
    private String name;
    private LocalDate birthday;
}
