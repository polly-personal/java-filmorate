package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {
    private MpaDao mpaDao;

    public MpaServiceImpl(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    @Override
    public Mpa getById(long id) {
        return mpaDao.getById(id);
    }

    public List<Mpa> getMpaList() {
        return mpaDao.getMpaList();
    }
}
