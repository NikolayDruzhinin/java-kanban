package ru.yandex.practicum.java_kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;
import ru.yandex.practicum.java_kanban.util.Managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {
    @BeforeEach
    @Override
    public void setup() {
        taskManager = Managers.getDefaultFileManager();
        super.setup();
    }

    @Test
    public void emptyFileLoadTest() {
        Path path = Paths.get("emptyFileTest.txt");
        taskManager = Managers.getCustomFileManager(path);
        assertEquals(taskManager.getTasks().size(), 0);
    }

    @Test
    public void saveSomeTasksTest() throws IOException {
        Path path = Paths.get("saveSomeTasksTest.txt");
        taskManager = Managers.getCustomFileManager(path);
        Task task = new Task("t1", "d1");
        taskManager.createTask(task);
        Epic epic = new Epic("e1", "d2");
        taskManager.createTask(epic);
        Subtask subtask = new Subtask("s1", "d3", epic);
        taskManager.createTask(subtask);
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            br.readLine();
            String[] taskData = br.readLine().split(",");
            Long id = Long.parseLong(taskData[0]);
            String name = taskData[2];
            TaskStatus taskStatus = TaskStatus.fromString(taskData[3]);
            String description = taskData[4];
            assertEquals(task.getId(), id);
            assertEquals(task.getName(), name);
            assertEquals(task.getDescription(), description);
            assertEquals(task.getStatus(), taskStatus);

            taskData = br.readLine().split(",");
            id = Long.parseLong(taskData[0]);
            name = taskData[2];
            taskStatus = TaskStatus.fromString(taskData[3]);
            description = taskData[4];
            assertEquals(epic.getId(), id);
            assertEquals(epic.getName(), name);
            assertEquals(epic.getDescription(), description);
            assertEquals(epic.getStatus(), taskStatus);

            taskData = br.readLine().split(",");
            id = Long.parseLong(taskData[0]);
            name = taskData[2];
            taskStatus = TaskStatus.fromString(taskData[3]);
            description = taskData[4];
            Long epicId = Long.parseLong(taskData[5]);
            assertEquals(subtask.getId(), id);
            assertEquals(subtask.getName(), name);
            assertEquals(subtask.getDescription(), description);
            assertEquals(subtask.getStatus(), taskStatus);
            assertEquals(subtask.getEpic().getId(), epicId);
        }
    }
}
