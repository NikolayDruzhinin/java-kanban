package ru.yandex.practicum.java_kanban.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.service.TaskManager;
import ru.yandex.practicum.java_kanban.util.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    public void shouldBeEqualTaskSameIdTest() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("Task1", "Desc1");
        taskManager.createTask(task1);
        Task task2 = taskManager.getTask(task1.getId());
        assertEquals(task1, task2);
    }
}
