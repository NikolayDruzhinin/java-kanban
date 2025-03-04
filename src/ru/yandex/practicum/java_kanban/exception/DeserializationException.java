package ru.yandex.practicum.java_kanban.exception;

public class DeserializationException extends RuntimeException {
    public DeserializationException(final String msg) {
        super(msg);
    }
}
