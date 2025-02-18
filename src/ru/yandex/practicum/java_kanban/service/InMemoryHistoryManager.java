package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.util.CustomLinkedList;

import java.util.List;

public class InMemoryHistoryManager<T extends Task> implements HistoryManager<T> {
    private final CustomLinkedList taskHistory;

    public InMemoryHistoryManager() {
        super();
        taskHistory = new CustomLinkedList();
    }

    @Override
    public List<T> getHistory() {
        return taskHistory.getTasks();
    }

    @Override
    public void add(T task) {
        synchronized (taskHistory) {
            taskHistory.linkLast(task);
        }
    }

    @Override
    public void remove(Long id) {
        Task task = taskHistory.get(id);
        if (task instanceof Epic) {
            Epic epic = (Epic) task;
            epic.getSubtasks().forEach(subtask -> taskHistory.remove(subtask.getId()));
        }
        taskHistory.remove(id);
    }

}
