package ru.yandex.practicum.java_kanban.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;

public class SubtaskTests extends Tests<Subtask> {

    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    public void createSubtask() {
        epic = new Epic("epic1", "description1");
        subtask = new Subtask("subtask1", "description1", epic);
    }

    @Test
    public void shouldCreateTaskTest() {
        createTest(subtask);
    }

    @Test
    public void shouldGetTaskTest() {
        getTest(subtask);
    }

    @Test
    public void shouldDeleteTaskTest() {
        deleteTest(subtask);
    }

    @Test
    public void shouldGetTasksTest() {
        Subtask subtask2 = new Subtask("subtask2", "description2", epic);
        Subtask subtask3 = new Subtask("subtask3", "description3", epic);
        getTest(subtask3, subtask2, subtask3);
    }

    @Test
    public void shouldUpdateTasksTest() {
        updateTest(subtask);
    }
}
