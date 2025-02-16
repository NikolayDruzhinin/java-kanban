package ru.yandex.practicum.java_kanban.util;

public class IntersectionException extends RuntimeException {
    public IntersectionException(final String msg) {
        super(msg);
    }

    public IntersectionException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
