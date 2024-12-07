package ru.yandex.practicum.java_kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    public HistoryManager tm ;

    @BeforeEach
    public void setup() {
        tm = new InMemoryHistoryManager();
    }

    @Test
    public void shouldStoreCurrentTaskDataTest() {
        Task task1 = new Task("task1", "desc1");
        tm.createTask(task1);
        tm.getTask(task1.getId());
        Task task2 = new Task(task1);
        task1.setStatus(TaskStatus.DONE);
        tm.getTask(task1.getId());
        Task task3 = new Task(task1);
        List<Task> tasks = tm.getHistory();
        assertEquals(2, tasks.size());
        assertEquals(task2, tasks.getFirst());
        assertEquals(task2.getName(), tasks.getFirst().getName());
        assertEquals(task2.getDescription(), tasks.getFirst().getDescription());
        assertEquals(task2.getStatus(), tasks.getFirst().getStatus());
        assertEquals(task3, tasks.get(1));
        assertEquals(task3.getStatus(), tasks.get(1).getStatus());
        assertEquals(task3.getName(), tasks.get(1).getName());
        assertEquals(task3.getDescription(), tasks.get(1).getDescription());
    }

    @Test
    public void shouldStoreTenElementsTest() {
        Task[] tasks = new Task[10];
        for (int i = 0; i < 10; ++i) {
            Task task = new Task("Task" + i, "Desc" + i);
            tasks[i] = task;
            tm.createTask(task);
            tm.getTask(task.getId());
        }
        assertArrayEquals(tasks, tm.getHistory().toArray());
        Task task11 = new Task("Task11", "Desc11");
        tm.createTask(task11);
        tm.getTask(task11.getId());
        assertEquals(task11, tm.getHistory().getFirst());
    }
}