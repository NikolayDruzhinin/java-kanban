package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.logging.Logger;

public class InMemoryHistoryManager<T extends Task> extends InMemoryTaskManager<T> implements HistoryManager<T> {
    private static final Logger logger = Logger.getLogger(InMemoryHistoryManager.class.getName());
    private final Deque<T> taskHistory;

    public InMemoryHistoryManager() {
        super();
        taskHistory = new ArrayDeque<>();
    }

    @Override
    public T getTask(long id) {
        if (this.tasks.containsKey(id)) {
            try {
                T task = tasks.get(id);
                T newTask = (T) task.getClass().getDeclaredConstructor(task.getClass()).newInstance(task);
                add(newTask);

            } catch (InstantiationException | IllegalAccessException |
                     InvocationTargetException | NoSuchMethodException e) {
                logger.severe(e.getMessage());
            }
            return tasks.get(id);
        }
        return null;
    }

    @Override
    public List<T> getHistory() {
        return new ArrayList<>(taskHistory);
    }

    @Override
    public void add(T task) {
        synchronized (taskHistory) {
            if (taskHistory.size() == 10) {
                taskHistory.poll();
                taskHistory.addFirst(task);
            } else {
                taskHistory.add(task);
            }
        }
    }
}
