package ru.yandex.practicum.java_kanban.model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected long id;

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public Task(String name, String description, long id) {
        this(name, description);
        this.id = id;
    }

    public Task(Task task) {
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
        this.id = task.id;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(TaskType.valueOf(getClass().getSimpleName().toUpperCase()));
        sb.append(",");
        sb.append(name);
        sb.append(",");
        sb.append(status);
        sb.append(",");
        sb.append(description);
        return sb.toString();
    }
}
