package ru.yandex.practicum.java_kanban.model;

public enum TaskStatus {
    NEW, IN_PROGRESS, DONE;

    public static TaskStatus fromString(String value) {
        switch (value.toLowerCase()) {
            case "new" -> {
                return NEW;
            } case "in_progress" -> {
                return IN_PROGRESS;
            } case "done" -> {
                return DONE;
            } default -> throw new IllegalArgumentException("Unknown task status: " + value);
        }
    }
}
