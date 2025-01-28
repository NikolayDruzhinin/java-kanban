package ru.yandex.practicum.java_kanban.util;

import ru.yandex.practicum.java_kanban.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static TaskManager fileBackedTaskManager;

    private Managers() {
    }

    public static TaskManager getDefaultFileBackedTaskManager() {
        Path path = Paths.get("defaultFileManagerTest.txt");
        return getFileBackedTaskManager(path);
    }

    public static TaskManager getCustomFileBackedTaskManager(Path path) {
        return getFileBackedTaskManager(path);
    }

    private static TaskManager getFileBackedTaskManager(Path path) {
        try {
            fileBackedTaskManager = Files.exists(path) ? new FileBackedTaskManager(path) :
                    new FileBackedTaskManager(Files.createFile(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBackedTaskManager;
    }

    public static TaskManager getDefaultInMemoryManager() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistoryManager() {
        return historyManager;
    }

}
