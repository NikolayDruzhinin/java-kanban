package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final Deque<Task> taskHistory;

    public InMemoryHistoryManager() {
        super();
        taskHistory = new ArrayDeque<>();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(taskHistory);
    }

    @Override
    public void add(Task task) {
        Task tmp = new Task(task);
        synchronized (taskHistory) {
            if (taskHistory.size() == 10) {
                taskHistory.poll();
            }
            taskHistory.addLast(tmp);
        }
    }
}
