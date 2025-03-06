package ru.yandex.practicum.java_kanban.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(final String msg) {
        super(msg);
    }

    public NotFoundException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
