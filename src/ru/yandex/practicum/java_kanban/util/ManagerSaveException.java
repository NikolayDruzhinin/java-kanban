package ru.yandex.practicum.java_kanban.util;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String msg) {
        super(msg);
    }

    public ManagerSaveException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
