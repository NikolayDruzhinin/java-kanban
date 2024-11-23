package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class TaskManager {
    private static Map<Long, Task> tasks = new HashMap<>();
    private static final TaskManager instance = new TaskManager();

    private TaskManager() {
    }

    public static TaskManager getInstance() {
        return instance;
    }

    public List<Task> getTasks(Class<? extends Task> t) {
        return tasks.values().stream().filter(task -> task.getClass() == t).collect(Collectors.toList());
    }

    public void removeTasks(Class<? extends Task> t) {
        synchronized (tasks) {
            List<Task> removeTasksList = getTasks(t);
            removeTasksList.stream().forEach(task -> tasks.remove(task.getId()));

            //if there are subtasks in removal list clear them from epics
            removeTasksList.stream().filter(element -> element instanceof Subtask)
                    .forEach(subtask -> ((Subtask) subtask).removeSubtaskFromEpic());
        }
    }

    public Task getTask(long id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        createTask(task);
    }

    public void clearData() {
        tasks.clear();
    }

    public void removeById(long id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }
}
