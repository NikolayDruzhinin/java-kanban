package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    Logger logger = Logger.getLogger(InMemoryTaskManager.class.getName());
    protected final Map<Long, T> tasks;
    protected final AtomicLong idCounter;

    public InMemoryTaskManager() {
        idCounter = new AtomicLong(0);
        tasks = new HashMap<>();
    }

    @Override
    public List<Task> getTasks() {
        return (List<Task>) tasks.values().stream()
                .filter(task -> !(task instanceof Epic || task instanceof Subtask)).toList();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return (List<Subtask>) tasks.values().stream().filter(task -> task instanceof Subtask).toList();
    }

    @Override
    public List<Epic> getEpics() {
        return (List<Epic>) tasks.values().stream().filter(task -> task instanceof Epic).toList();
    }

    @Override
    public T getTask(long id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public List<Subtask> getEpicSubtasks(long id) {
        T epic = tasks.get(id);
        if (epic instanceof Epic) {
            return new ArrayList<>(((Epic) epic).getSubtasks());
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
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task t) {
        long id = t.getId();
        if (tasks.containsKey(id) && tasks.get(id).getClass() == Task.class) {
            tasks.put(id, (T) t);
        } else {
            logger.warning("Can't update task, task with id = " + id + " not exists");
        }
    }

    @Override
    public void updateSubtask(Subtask t) {
        long id = t.getId();
        if (tasks.containsKey(id) && tasks.get(id).getClass() == Subtask.class) {
            tasks.put(id, (T) t);
        } else {
            logger.warning("Can't update subtask, subtask with id = " + id + " not exists");
        }
    }

    @Override
    public void updateEpic(Epic t) {
        long id = t.getId();
        if (tasks.containsKey(id) && tasks.get(id).getClass() == Epic.class) {
            tasks.put(id, (T) t);
        } else {
            logger.warning("Can't update epic, epic with id = " + id + " not exists");
        }
    }

}
