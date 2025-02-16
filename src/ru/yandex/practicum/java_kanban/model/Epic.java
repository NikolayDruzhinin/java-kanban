package ru.yandex.practicum.java_kanban.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public Epic(Epic epic) {
        super(epic);
        subtasks = List.copyOf(epic.subtasks);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public Subtask getSubtask(long id) {
        for (Subtask subtask : subtasks) {
            if (subtask.getId() == id) {
                return subtask;
            }
        }
        return null;
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        updateStatus();
    }

    public void removeSubtasks() {
        subtasks.clear();
    }

    public void updateStatus() {
        long subtasksNew = subtasks.stream().map(Subtask::getStatus)
                .filter(taskStatus -> taskStatus.equals(TaskStatus.NEW))
                .count();
        long subtasksDone = subtasks.stream().map(Subtask::getStatus)
                .filter(taskStatus -> taskStatus.equals(TaskStatus.DONE))
                .count();

        if (subtasks.isEmpty() || subtasksNew == subtasks.size()) {
            status = TaskStatus.NEW;
        } else if (subtasksDone == subtasks.size()) {
            status = TaskStatus.DONE;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }

        if (status.equals(TaskStatus.DONE)) {
            duration = subtasks.stream()
                    .filter(s -> s.getStatus().equals(TaskStatus.DONE))
                    .map(Task::getDuration)
                    .reduce((d1, d2) -> d1.plus(d2))
                    .get();
            endTime = subtasks.stream()
                    .filter(s -> s.getStatus().equals(TaskStatus.DONE))
                    .map(Task::getEndTime)
                    .max((task1, task2) -> task1.isAfter(task2) ? 1 : -1)
                    .get();
        }

        if (status.equals(TaskStatus.IN_PROGRESS)) {
            startTime = subtasks.stream()
                    .filter(s -> s.getStartTime() != null)
                    .min((task1, task2) -> {
                        if (task1.getStartTime().equals(task2.getStartTime())) {
                            return 0;
                        } else {
                            return task1.getStartTime().isBefore(task2.getStartTime()) ? 1 : -1;
                        }
                    })
                    .get().getStartTime();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Epic{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", status=").append(status)
                .append(", subtasksIds=").append(subtasks.stream().map(Subtask::getId).toList());

        if (startTime != null) {
            sb.append(", startTime=").append(startTime).append('\'');
        }
        if (duration != null) {
            sb.append(", duration").append(duration).append('\'');
        }
        sb.append("}");

        return sb.toString();
    }
}
