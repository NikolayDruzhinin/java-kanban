package ru.yandex.practicum.java_kanban.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.service.TaskManager;
import ru.yandex.practicum.java_kanban.util.Managers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {
    private static final TaskManager taskManager = Managers.getDefault();

    @Test
    public void areEqualEpicsSameIdTest() {
        Epic epic1 = new Epic("Epic1", "Desc1");
        taskManager.createTask(epic1);
        Epic epic2 = (Epic) taskManager.getTask(epic1.getId());
        assertEquals(epic1, epic2);
    }

}