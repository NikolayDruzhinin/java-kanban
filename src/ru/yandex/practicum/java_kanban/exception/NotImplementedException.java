package ru.yandex.practicum.java_kanban.exception;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException(final String msg) {
        super(msg);
    }

    public NotImplementedException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
