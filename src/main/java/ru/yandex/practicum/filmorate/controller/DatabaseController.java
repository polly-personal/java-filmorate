package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.storage.dao.ManagerDatabaseDao;

@RestController
@RequestMapping("/database")
@Slf4j
public class DatabaseController {
    private ManagerDatabaseDao managerDatabase;

    @Autowired
    public DatabaseController(ManagerDatabaseDao managerDatabase) {
        this.managerDatabase = managerDatabase;
    }

    @PostMapping("/create")
    public void createAllTabs() {
        managerDatabase.createAllTabs();
        log.info("🟧 все таблицы созданы");
    }


    @DeleteMapping
    public void deleteAllTabs() {
        managerDatabase.deleteAllTabs();
        log.info("🟧 все таблицы удалены");
    }

    @DeleteMapping("/users")
    public void deleteAllTabsAssociatedWithUserTable() {
        managerDatabase.deleteAllTabsAssociatedWithUserTable();
        log.info("🟧 все таблицы, связанные с таблицой \"user\" (включая её), удалены");
    }

    @DeleteMapping("/films")
    public void deleteAllTabsAssociatedWithFilmTable() {
        managerDatabase.deleteAllTabsAssociatedWithFilmTable();
        log.info("🟧 все таблицы, связанные с таблицой \"film\" (включая её), удалены");
    }
}
