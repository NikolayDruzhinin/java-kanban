package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;

import java.util.List;

public interface TaskManager<T extends Task> {
    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    T getTask(long id);

    List<Subtask> getEpicSubtasks(long id);

    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    void removeTask(T t);

    void createTask(T t);

    void updateTask(T t);

    List<T> getPrioritizedTasks();
}
