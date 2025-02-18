package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.List;

public interface HistoryManager<T extends Task> {
    void add(T task);

    void remove(Long id);

    List<T> getHistory();
}
