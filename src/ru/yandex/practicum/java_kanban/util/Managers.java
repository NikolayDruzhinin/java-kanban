package ru.yandex.practicum.java_kanban.util;

import ru.yandex.practicum.java_kanban.service.HistoryManager;
import ru.yandex.practicum.java_kanban.service.InMemoryHistoryManager;
import ru.yandex.practicum.java_kanban.service.InMemoryTaskManager;
import ru.yandex.practicum.java_kanban.service.TaskManager;

public class Managers {
    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager inMemoryTaskManager = new InMemoryTaskManager();

    private Managers() {
    }

    public static TaskManager getDefault() {
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
