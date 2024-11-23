package ru.yandex.practicum.java_kanban.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void updateStatus() {
        long subtasksNew = subtasks.stream().filter(subtask -> subtask.getStatus() == TaskStatus.NEW).count();
        long subtasksDone = subtasks.stream().filter(subtask -> subtask.getStatus() == TaskStatus.DONE).count();
        if (subtasksDone == subtasks.size()) {
            status = TaskStatus.DONE;
        } else if (subtasksNew == subtasks.size()) {
            status = TaskStatus.NEW;
        } else {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasksIds=" + subtasks.stream().map(Subtask::getId).toList() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
