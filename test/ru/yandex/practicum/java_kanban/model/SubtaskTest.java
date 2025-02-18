package ru.yandex.practicum.java_kanban.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.service.TaskManager;
import ru.yandex.practicum.java_kanban.util.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    @Test
    public void shouldBeEqualSubtaskSameIdTest() {
        TaskManager taskManager = Managers.getDefaultInMemoryManager();
        Epic epic1 = new Epic("Epic1", "Desc1");
        Subtask subtask1 = new Subtask("Subtask1", "Desc1", epic1);
        taskManager.createTask(subtask1);
        Subtask subtask2 = (Subtask) taskManager.getTask(subtask1.getId());
        assertEquals(subtask1, subtask2);
    }
}