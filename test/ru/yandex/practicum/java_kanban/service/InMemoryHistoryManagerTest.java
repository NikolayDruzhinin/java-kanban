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
        task1.setId(1);
        historyTm.add(task1);
        List<Task> tasks = historyTm.getHistory();
        assertEquals(1, tasks.size());
        assertEquals(task1, tasks.getFirst());

        task1.setStatus(TaskStatus.DONE);
        historyTm.add(task1);
        tasks = historyTm.getHistory();
        assertEquals(1, tasks.size());
        assertEquals(task1.getStatus(), tasks.getFirst().getStatus());
    }

    @Test
    public void shouldStoreTenElementsTest() {
        Task[] tasks = new Task[10];
        for (int i = 0; i < 10; ++i) {
            Task task = new Task("Task" + i, "Desc" + i);
            task.setId(i);
            tasks[i] = task;
            historyTm.add(task);
        }
        assertArrayEquals(tasks, historyTm.getHistory().toArray());
        Task task11 = new Task("Task11", "Desc11");
        historyTm.add(task11);
        assertEquals(task11, historyTm.getHistory().getFirst());
    }

    @Test
    public void shouldRemoveTask() {
        Task task = new Task("task1", "description1");
        task.setId(1);
        historyTm.add(task);
        historyTm.remove(task.getId());
        assertEquals(0, historyTm.getHistory().size());
    }

    @Test
    public void shouldRemoveEpicSubtasks() {
        Epic epic = new Epic("epic1", "description1", 1);
        Subtask subtask = new Subtask("subtask1", "description2", epic, 2);
        historyTm.add(epic);
        historyTm.add(subtask);
        historyTm.remove(epic.getId());
        assertEquals(0, historyTm.getHistory().size());
    }
}