package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private final int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
