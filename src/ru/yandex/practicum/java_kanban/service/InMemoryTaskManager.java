package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    protected final Map<Long, T> tasks;
    protected final AtomicLong idCounter;

    public InMemoryTaskManager() {
        idCounter = new AtomicLong(0);
        tasks = new HashMap<>();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values().stream()
                .filter(task -> !(task instanceof Epic || task instanceof Subtask)).toList());
    }

    @Override
    public List<T> getSubtasks() {
        return new ArrayList<>(tasks.values().stream().filter(task -> task instanceof Subtask).toList());
    }

    @Override
    public List<T> getEpics() {
        return new ArrayList<>(tasks.values().stream().filter(task -> task instanceof Epic).toList());
    }

    @Override
    public T getTask(long id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public void removeTasks() {
        tasks.values().removeIf(task -> !(task instanceof Subtask || task instanceof Epic));
    }

    @Override
    public void removeSubtasks() {
        tasks.values().stream().filter(task -> task instanceof Epic).forEach(epic -> {
            ((Epic) epic).removeSubtasks();
            ((Epic) epic).updateStatus();
        });
        tasks.values().removeIf(task -> task instanceof Subtask);
    }

    @Override
    public void removeEpics() {
        removeSubtasks();
        tasks.values().removeIf(task -> task instanceof Epic);

    }

    @Override
    public void removeTask(T task) {
        if (tasks.containsKey(task.getId())) {
            tasks.remove(task.getId());
            if (task instanceof Subtask tmpSubtask) {
                tmpSubtask.removeEpic();
            } else if (task instanceof Epic) {
                ((Epic) task).getSubtasks().forEach(subtask -> removeTask((T) subtask));
                ((Epic) task).removeSubtasks();
            }
        }
    }

    @Override
    public void createTask(T task) {
        if (task.getId() == 0) {
            task.setId(idCounter.incrementAndGet());
        }
        updateTask(task);
    }

    @Override
    public void updateTask(T task) {
        tasks.put(task.getId(), task);
    }

}
