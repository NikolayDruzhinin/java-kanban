package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.List;

public interface HistoryManager<T extends Task> extends TaskManager<T> {
    void add(T task);

    List<T> getHistory();
}
