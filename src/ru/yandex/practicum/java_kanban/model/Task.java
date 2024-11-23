package ru.yandex.practicum.java_kanban.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected long id;
    private static AtomicLong idCounter = new AtomicLong(0);

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        id = idCounter.incrementAndGet();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
