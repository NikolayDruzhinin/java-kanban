package ru.yandex.practicum.java_kanban.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Task;

public class TaskTests extends Tests<Task> {
    private Task task;

    @BeforeEach
    public void createTask() {
        task = new Task("task1", "description1");
    }

    @Test
    public void shouldCreateTaskTest() {
        createTest(task);
    }

    @Test
    public void shouldGetTaskTest() {
        getTest(task);
    }

    @Test
    public void shouldDeleteTaskTest() {
        deleteTest(task);
    }

    @Test
    public void shouldGetTasksTest() {
        Task task2 = new Task("task2", "description2");
        Task task3 = new Task("task3", "description3");
        getTest(task, task2, task3);
    }

    @Test
    public void shouldUpdateTasksTest() {
        updateTest(task);
    }

}
