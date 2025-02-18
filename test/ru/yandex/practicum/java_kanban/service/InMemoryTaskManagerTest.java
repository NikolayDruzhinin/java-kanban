package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.util.Managers;

public class InMemoryTaskManagerTest<T extends Task> extends TaskManagerTest {
    public InMemoryTaskManagerTest() {
        taskManager = Managers.getDefaultInMemoryManager();
    }
}
