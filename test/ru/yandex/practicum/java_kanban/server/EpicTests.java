package ru.yandex.practicum.java_kanban.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;

public class EpicTests extends Tests<Epic> {
    private Epic epic;

    @BeforeEach
    public void createEpic() {
        epic = new Epic("epic1", "description1");
    }

    @Test
    public void shouldCreateTaskTest() {
        createTest(epic);
    }

    @Test
    public void shouldGetTaskTest() {
        getTest(epic);
    }

    @Test
    public void shouldDeleteTaskTest() {
        deleteTest(epic);
    }

    @Test
    public void shouldGetTasksTest() {
        Epic epic2 = new Epic("epic2", "description2");
        Epic epic3 = new Epic("epic3", "description3");
        getTest(epic, epic2, epic3);
    }

    @Test
    public void shouldUpdateTasksTest() {
        updateTest(epic);
    }
}
