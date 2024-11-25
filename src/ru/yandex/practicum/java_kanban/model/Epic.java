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
    }

    public void removeSubtasks() {
        subtasks.forEach(Subtask::nullifyEpic);
        subtasks.clear();
    }

    public void updateStatus() {
        long subtasksNew = 0;
        long subtasksDone = 0;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                subtasksNew++;
            } else if (subtask.getStatus() == TaskStatus.DONE) {
                subtasksDone++;
            }
        }

        if (subtasks.isEmpty() || subtasksNew == subtasks.size()) {
            status = TaskStatus.NEW;
        } else  if (subtasksDone == subtasks.size()) {
            status = TaskStatus.DONE;
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
