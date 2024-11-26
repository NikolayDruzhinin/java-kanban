package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TaskManager {
    private final Map<Long, Task> tasks;
    private final Map<Long, Subtask> subtasks;
    private final Map<Long, Epic> epics;
    private final AtomicLong idCounter;

    public TaskManager() {
        idCounter = new AtomicLong(0);
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values().stream().toList());
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values().stream().toList());
    }

    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values().stream().toList());
    }

    public Task getTask(long id) {
        return tasks.get(id);
    }

    public Subtask getSubtask(long id) {
        return subtasks.get(id);
    }

    public Epic getEpic(long id) {
        return epics.get(id);
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks() {
        subtasks.values().forEach(subtask -> {
            Epic e = subtask.getEpic();
            e.removeSubtask(subtask);
            e.updateStatus();
            subtask.removeEpic();
        });
        subtasks.clear();
    }

    public void removeEpics() {
        epics.values().forEach(epic -> epic.removeSubtasks());
        epics.clear();
    }


    public void removeTask(long id) {
        tasks.remove(id);
    }

    public void removeSubtask(long id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            subtask.removeEpic();
            subtasks.remove(id);
        }
    }

    public void removeEpic(long id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epic.removeSubtasks();
            epics.remove(id);
        }
    }

    public void createTask(Task task) {
        task.setId(idCounter.incrementAndGet());
        tasks.put(task.getId(), task);
    }

    public void createSubtask(Subtask task) {
        task.setId(idCounter.incrementAndGet());
        subtasks.put(task.getId(), task);
    }

    public void createEpic(Epic task) {
        task.setId(idCounter.incrementAndGet());
        epics.put(task.getId(), task);
    }

}
