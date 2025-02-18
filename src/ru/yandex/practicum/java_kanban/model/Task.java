package ru.yandex.practicum.java_kanban.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected long id;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        startTime = LocalDateTime.of(1970, 1, 1,
                0, 0, 0, 0);
    }

    public Task(Task task) {
        this.name = task.name;
        this.description = task.description;
        this.status = task.status;
        this.id = task.id;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        if (status.equals((TaskStatus.IN_PROGRESS))) {
            startTime = LocalDateTime.now();
        }

        if (status.equals(TaskStatus.DONE)) {
            duration = Duration.between(startTime, LocalDateTime.now());
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return Duration.ofSeconds(duration.getSeconds());
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setId(long id) {
        this.id = id;
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

        if (startTime != null) {
            sb.append(",");
            sb.append(startTime);
        }
        if (duration != null) {
            sb.append(",");
            sb.append(duration.toMinutes());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", status=").append(status);

        if (startTime != null) {
            sb.append(", startTime=").append(startTime).append('\'');
        }
        if (duration != null) {
            sb.append(", duration=").append(duration).append('\'');
        }
        sb.append("}");

        return sb.toString();
    }
}
