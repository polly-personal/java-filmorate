package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private GenreDao genreDao;

    public GenreServiceImpl(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    @Override
    public Genre getById(long id) {
        return genreDao.getById(id);
    }

    @Override
    public List<Genre> getGenresList() {
        return genreDao.getGenreList();
    }
}
