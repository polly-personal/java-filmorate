package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private String email;
    private long id;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendIds;
    private Set<Long> likedFilms;
}
