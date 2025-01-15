package ru.yandex.practicum.java_kanban.util;

import ru.yandex.practicum.java_kanban.model.Task;

import java.util.Objects;

public class Node<T extends Task> {
    public T task;
    public Node next;
    public Node prev;

    public Node(Node prev, T task, Node next) {
        this.prev = prev;
        this.task = task;
        this.next = next;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return this.task.equals(node.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task);
    }
}
