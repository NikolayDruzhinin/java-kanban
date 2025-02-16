package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;
import ru.yandex.practicum.java_kanban.util.IntersectionException;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    private Logger logger = Logger.getLogger(InMemoryTaskManager.class.getName());
    protected final Map<Long, T> tasks;
    protected final AtomicLong idCounter;
    protected Set<T> prioritizedTasks;

    public InMemoryTaskManager() {
        idCounter = new AtomicLong(0);
        tasks = new HashMap<>();
        prioritizedTasks = new TreeSet<>((task1, task2) -> {
            if (task1.getStartTime().equals(task2.getStartTime())) {
                return 0;
            } else {
                return task1.getStartTime().isBefore(task2.getStartTime()) ? 1 : -1;
            }
        });
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values().stream()
                .filter(task -> !(task instanceof Epic || task instanceof Subtask))
                .map(task -> (Task) task)
                .toList());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(tasks.values().stream()
                .filter(task -> task instanceof Subtask)
                .map(subtask -> (Subtask) subtask)
                .toList());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(tasks.values().stream()
                .filter(task -> task instanceof Epic)
                .map(epic -> (Epic) epic)
                .toList());
    }

    @Override
    public T getTask(long id) {
        return tasks.get(id);
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
        prioritizedTasks.removeIf(task -> !(task instanceof Subtask || task instanceof Epic));
    }

    @Override
    public void removeSubtasks() {
        tasks.values().stream()
                .filter(e -> e instanceof Epic)
                .map(e -> (Epic) e)
                .forEach(e -> {
                    e.removeSubtasks();
                    e.updateStatus();
                });
        tasks.values().stream().filter(task -> task instanceof Subtask)
                .map(s -> (Subtask) s)
                .forEach(Subtask::removeEpic);
        tasks.values().removeIf(s -> s instanceof Subtask);
        prioritizedTasks.removeIf(s -> s instanceof Subtask);
    }

    @Override
    public void removeEpics() {
        removeSubtasks();
        tasks.values().removeIf(e -> e instanceof Epic);
    }

    @Override
    public void removeTask(T task) {
        if (tasks.containsKey(task.getId())) {
            tasks.remove(task.getId());
            prioritizedTasks.remove(task);
            if (task instanceof Subtask tmpSubtask) {
                Epic epic = tmpSubtask.getEpic();
                epic.removeSubtask(tmpSubtask);
                epic.updateStatus();
                tmpSubtask.removeEpic();
            } else if (task instanceof Epic) {
                ((Epic) task).getSubtasks().forEach(Subtask::removeEpic);
                ((Epic) task).removeSubtasks();
            }
        }
    }

    @Override
    public void createTask(T task) {
        if (task.getId() == 0) {
            task.setId(idCounter.incrementAndGet());
        }

        if (task instanceof Subtask) {
            Epic epic = ((Subtask) task).getEpic();
            epic.updateStatus();
            if (!epic.getSubtasks().contains(task)) {
                epic.addSubtask((Subtask) task);
            }
        }
        task.setStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateTask(T task) {
        long id = task.getId();
        if (tasks.containsKey(id)) {
            if (task.getStatus().equals(TaskStatus.DONE)) {
                checkIsTaskIntersect(task);
            }

            if (task instanceof Subtask) {
                Epic epic = ((Subtask) task).getEpic();
                if (epic != null) {
                    if (!epic.getSubtasks().contains(task)) {
                        epic.addSubtask((Subtask) task);
                    } else {
                        epic.updateStatus();
                    }
                }
            }

            tasks.put(task.getId(), task);
            if (!(task instanceof Epic) && !task.getStatus().equals(TaskStatus.NEW)) {
                prioritizedTasks.add(task);
            }
        } else {
            logger.warning("Can't update task, task with id = " + id + " not exists");
        }
    }

    @Override
    public List<T> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private void checkIsTaskIntersect(T task) {
        prioritizedTasks.stream()
                .filter(t -> t.getStatus().equals(TaskStatus.DONE) && !t.equals(task))
                .forEach(t -> {
                    if (t.getEndTime().isAfter(task.getStartTime())) {
                        throw new IntersectionException("You can't progress task " + task + ", it intersects with task " + t);
                    }
                });
    }
}
