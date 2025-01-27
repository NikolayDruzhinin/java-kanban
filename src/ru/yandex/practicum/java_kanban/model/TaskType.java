package ru.yandex.practicum.java_kanban.model;

public enum TaskType {
    TASK, SUBTASK, EPIC;

    public static TaskType fromString(String value) {
        switch (value.toLowerCase()) {
            case "task" -> {
                return TASK;
            } case "subtask" -> {
                return SUBTASK;
            } case "epic" -> {
                return EPIC;
            } default -> throw new IllegalArgumentException("Unknown task type: " + value);
        }
    }
}
