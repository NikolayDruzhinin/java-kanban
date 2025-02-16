package ru.yandex.practicum.java_kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest<T extends Task> {
    private HistoryManager historyTm;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    public void setup() {
        historyTm = new InMemoryHistoryManager();
        task = new Task("task1", "desc1");
        task.setId(1);
        epic = new Epic("epic1", "description1");
        epic.setId(2);
        subtask = new Subtask("subtask1", "description2", epic);
        subtask.setId(3);
        epic.addSubtask(subtask);
        historyTm.add(epic);
        historyTm.add(subtask);
        historyTm.add(task);
    }

    @Test
    public void shouldBeEmptyHistory() {
        historyTm = new InMemoryHistoryManager();
        assertTrue(historyTm.getHistory().isEmpty());
    }

    @Test
    public void shouldNotBeDoubledTasks() {
        historyTm.add(task);
        List<T> history = historyTm.getHistory();
        assertEquals(3, history.size());
        assertEquals(1, history.stream().filter(t -> t.equals(task)).count());
    }

    @Test
    public void shouldStoreCurrentTaskDataTest() {
        List<T> tasks = historyTm.getHistory();
        assertEquals(3, tasks.size());
        assertEquals(task, tasks.stream()
                .filter(t -> t.equals(task)).findFirst().get());

        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setStatus(TaskStatus.DONE);
        historyTm.add(task);
        tasks = historyTm.getHistory();
        assertEquals(3, tasks.size());
        assertEquals(task.getStatus(), tasks.stream()
                .filter(t -> t.equals(task)).findFirst().get().getStatus());
    }

    @Test
    public void shouldRemoveTask() {
        historyTm.remove(task.getId());
        List<T> history = historyTm.getHistory();
        assertEquals(0, history.stream().filter(t -> t.equals(task)).count());
    }

    @Test
    public void shouldRemoveEpicSubtasks() {
        historyTm.remove(epic.getId());
        historyTm.remove(task.getId());
        assertEquals(0, historyTm.getHistory().size());
    }
}