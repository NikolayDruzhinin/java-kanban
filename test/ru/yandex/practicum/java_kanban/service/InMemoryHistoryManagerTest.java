package ru.yandex.practicum.java_kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    public HistoryManager historyTm;

    @BeforeEach
    public void setup() {
        historyTm = new InMemoryHistoryManager();
    }

    @Test
    public void shouldStoreCurrentTaskDataTest() {
        Task task1 = new Task("task1", "desc1", 1);
        Task task2 = new Task(task1);
        historyTm.add(task1);
        task1.setStatus(TaskStatus.DONE);
        historyTm.add(task1);
        List<Task> tasks = historyTm.getHistory();
        assertEquals(2, tasks.size());
        assertEquals(task1.getStatus(), tasks.getLast().getStatus());
        assertEquals(task2.getStatus(), tasks.getFirst().getStatus());
    }

    @Test
    public void shouldStoreTenElementsTest() {
        Task[] tasks = new Task[10];
        for (int i = 0; i < 10; ++i) {
            Task task = new Task("Task" + i, "Desc" + i);
            tasks[i] = task;
            historyTm.add(task);
        }
        assertArrayEquals(tasks, historyTm.getHistory().toArray());
        Task task11 = new Task("Task11", "Desc11");
        historyTm.add(task11);
        assertEquals(task11, historyTm.getHistory().getFirst());
    }
}