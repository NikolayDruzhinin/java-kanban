package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.List;

public interface TaskManager<T extends Task> {
    List<Task> getTasks();

    List<T> getSubtasks();

    List<T> getEpics();

    T getTask(long id);

    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    void removeTask(T t);

    void createTask(T t);

    void updateTask(T t);
}
