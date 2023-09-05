package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.sql.SQLException;
import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaServiceImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public Mpa getById(long id) throws IdNotFoundException {
        idIsValid(id);
        return mpaDao.getById(id);
    }

    @Override
    public List<Mpa> getMpaList() throws SQLException {
        return mpaDao.getMpaList();
    }

    @Override
    public boolean idIsValid(long id) throws IdNotFoundException {
        if (id <= 0) {
            throw new IdNotFoundException("ваш id: " + id + " -- отрицательный либо равен 0");
        }

        if (!mpaDao.idIsExists(id)) {
            throw new IdNotFoundException("введен несуществующий id: " + id);
        }
        return true;
    }
}
