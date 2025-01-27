package ru.yandex.practicum.java_kanban.util;

import ru.yandex.practicum.java_kanban.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();
    private static TaskManager fileManager;

    private Managers() {
    }

    public static TaskManager getDefaultFileManager() {
        Path path = Paths.get("defaultFileManagerTest.txt");
        return getFileManager(path);
    }

    public static TaskManager getCustomFileManager(Path path) {
        return getFileManager(path);
    }

    private static TaskManager getFileManager(Path path) {
        try {
            if (!Files.exists(path)) {
                fileManager = new FileBackedTaskManager(Files.createFile(path));
            } else {
                fileManager = new FileBackedTaskManager(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileManager;
    }

    public static TaskManager getDefaultInMemoryManager() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistoryManager() {
        return historyManager;
    }

}
