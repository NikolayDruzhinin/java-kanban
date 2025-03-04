package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.exception.IntersectionException;
import ru.yandex.practicum.java_kanban.exception.NotFoundException;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;

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
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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
    public T getTask(long id) throws NotFoundException {
        T t = tasks.get(id);
        if (t == null) {
            throw new NotFoundException("Task with ID " + id + " not found");
        }
        return t;
    }

    @Override
    public List<Subtask> getEpicSubtasks(long id) throws NotFoundException {
        T epic = tasks.get(id);
        if (epic == null || !(epic instanceof Epic)) {
            throw new NotFoundException("Epic with ID " + id + " not found");
        }
        return new ArrayList<>(((Epic) epic).getSubtasks());
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
    public void removeTask(long id) throws NotFoundException {
        T task = tasks.get(id);
        if (tasks.containsKey(id)) {
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
        } else {
            throw new NotFoundException(task + " not found");
        }
    }

    public void clear() {
        removeTasks();
        removeSubtasks();
        removeEpics();
        idCounter.set(0);
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
    public void updateTask(T task) throws NotFoundException {
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
            throw new NotFoundException(task + " not found");
        }
    }

    @Override
    public List<T> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    private void checkIsTaskIntersect(T task) throws IntersectionException {
        prioritizedTasks.stream()
                .filter(t -> t.getStatus().equals(TaskStatus.DONE) && !t.equals(task))
                .forEach(t -> {
                    if (t.getEndTime().isAfter(task.getStartTime())) {
                        throw new IntersectionException("You can't progress task " + task + ", it intersects with task " + t);
                    }
                });
    }
}
