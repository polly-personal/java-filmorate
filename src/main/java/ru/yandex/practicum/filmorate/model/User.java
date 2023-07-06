package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class User {
    private String email;
    private int id;
    private String login;
    private String name;
    private LocalDate birthday;
}
