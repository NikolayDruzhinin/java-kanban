package ru.yandex.practicum.java_kanban.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;
import ru.yandex.practicum.java_kanban.util.Managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest {

    public FileBackedTaskManagerTest() {
        taskManager = Managers.getDefaultFileBackedTaskManager();
    }

    @AfterEach
    public void clearFile() throws IOException {
        Files.delete(Managers.PATH);
        Files.createFile(Managers.PATH);
    }

    @Test
    public void emptyFileLoadTest() {
        Path path = Paths.get("emptyFileTest.txt");
        taskManager = Managers.getCustomFileBackedTaskManager(path);
        assertEquals(taskManager.getTasks().size(), 0);
    }

    @Test
    public void saveSomeTasksTest() throws IOException {
        Path path = Paths.get("saveSomeTasksTest.txt");
        taskManager = Managers.getCustomFileBackedTaskManager(path);
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
            TaskStatus taskStatus = TaskStatus.valueOf(taskData[3]);
            String description = taskData[4];
            assertEquals(task.getId(), id);
            assertEquals(task.getName(), name);
            assertEquals(task.getDescription(), description);
            assertEquals(task.getStatus(), taskStatus);

            taskData = br.readLine().split(",");
            id = Long.parseLong(taskData[0]);
            name = taskData[2];
            taskStatus = TaskStatus.valueOf(taskData[3]);
            description = taskData[4];
            assertEquals(epic.getId(), id);
            assertEquals(epic.getName(), name);
            assertEquals(epic.getDescription(), description);
            assertEquals(epic.getStatus(), taskStatus);

            taskData = br.readLine().split(",");
            id = Long.parseLong(taskData[0]);
            name = taskData[2];
            taskStatus = TaskStatus.valueOf(taskData[3]);
            description = taskData[4];
            Long epicId = Long.parseLong(taskData[taskData.length - 1]);
            assertEquals(subtask.getId(), id);
            assertEquals(subtask.getName(), name);
            assertEquals(subtask.getDescription(), description);
            assertEquals(subtask.getStatus(), taskStatus);
            assertEquals(subtask.getEpic().getId(), epicId);
        }
    }
}
