package ru.yandex.practicum.java_kanban.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CustomLinkedListTest {
    private CustomLinkedList list;

    @BeforeEach
    public void init() {
        list = new CustomLinkedList();
    }

    @Test
    public void shouldCorrectlyAdd() {
        Task task1 = new Task("task1", "Description1");
        task1.setId(1);
        Task task2 = new Task("task2", "Description2");
        task2.setId(2);
        Task task3 = new Task("task3", "Description3");
        task3.setId(3);
        list.linkLast(task1);
        assertEquals(list.getHead().task, task1);
        assertEquals(list.getTail().task, task1);
        list.linkLast(task2);
        assertEquals(list.getHead().task, task1);
        assertEquals(list.getTail().task, task2);
        list.linkLast(task3);
        assertEquals(list.getHead().task, task1);
        assertEquals(list.getTail().task, task3);
        assertEquals(list.getSize(), 3);
        Node head = list.getHead();
        assertEquals(head.task, task1);
        assertEquals(head.next.task, task2);
        assertEquals(head.next.next.task, task3);

        assertEquals(head, list.getIds().get(task1.getId()));
        assertEquals(head.next, list.getIds().get(task2.getId()));
        assertEquals(head.next.next, list.getIds().get(task3.getId()));
    }

    @Test
    public void shouldCorrectlyRemove() {
        Task task1 = new Task("task1", "Description1");
        task1.setId(1);
        Task task2 = new Task("task2", "Description2");
        task2.setId(2);
        Task task3 = new Task("task3", "Description3");
        task3.setId(3);
        list.linkLast(task1);
        list.linkLast(task2);
        list.linkLast(task3);
        list.remove(task2.getId());

        Node head = list.getHead();
        Node tail = list.getTail();
        assertEquals(head.next, tail);
        assertEquals(head.next.task, task3);
        assertEquals(tail.prev, head);
        assertEquals(tail.prev.task, task1);

        List<Task> tasks = list.getTasks();
        for (Task task : tasks)
            assertNotEquals(task, task2);

        assertEquals(null, list.getIds().get(task2));
    }
}
