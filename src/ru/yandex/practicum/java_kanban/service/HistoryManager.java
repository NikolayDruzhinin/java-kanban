package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
