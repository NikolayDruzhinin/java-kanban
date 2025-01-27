package ru.yandex.practicum.java_kanban.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;
import ru.yandex.practicum.java_kanban.util.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Subtask subtask3;
    protected TaskManager taskManager = Managers.getDefaultInMemoryManager();

    @BeforeEach
    public void setup() {
        taskManager.removeTasks();
        taskManager.removeSubtasks();
        taskManager.removeEpics();
        task1 = new Task("Task1", "Description1");
        task2 = new Task("Task2", "Description2");
        epic1 = new Epic("Epic1", "EpicDescription1");
        epic2 = new Epic("Epic2", "EpicDescription2");
        subtask1 = new Subtask("Subtask1", "SubtaskDescription1", epic1);
        subtask2 = new Subtask("Subtask2", "SubtaskDescription2", epic1);
        subtask3 = new Subtask("Subtask2", "SubtaskDescription2", epic2);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);
        taskManager.createTask(subtask3);
    }

    @Test
    public void getTasksTest() {
        List<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    public void getSubtasksTest() {
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(3, subtasks.size());
    }

    @Test
    public void getEpicsListTest() {
        List<Epic> epics = taskManager.getEpics();
        assertEquals(2, epics.size());
    }

    @Test
    public void updateStatusTest() {
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.DONE);
        assertEquals(task1.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(task2.getStatus(), TaskStatus.DONE);
    }

    @Test
    public void updateSubtasksStatusTest() {
        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(subtask1.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS);

        subtask2.setStatus(TaskStatus.IN_PROGRESS);
        assertEquals(subtask2.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS);

        subtask3.setStatus(TaskStatus.DONE);
        assertEquals(subtask3.getStatus(), TaskStatus.DONE);
        assertEquals(epic2.getStatus(), TaskStatus.DONE);

        subtask2.setEpic(epic2);
        assertEquals(epic2.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    public void removeTasksTest() {
        taskManager.removeTasks();
        assertEquals(0, taskManager.getTasks().size());
        assertNotEquals(0, taskManager.getSubtasks().size());
        assertNotEquals(0, taskManager.getEpics().size());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.DONE);
        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS);

        taskManager.removeTask(subtask1);
        assertNull(subtask1.getEpic());
        assertNull(epic1.getSubtask(subtask1.getId()));
        assertEquals(epic1.getStatus(), TaskStatus.DONE);

        taskManager.removeTask(subtask2);
        assertNull(subtask2.getEpic());
        assertNull(epic1.getSubtask(subtask2.getId()));
        assertEquals(epic1.getStatus(), TaskStatus.NEW);

        taskManager.removeTask(epic2);
        assertNull(taskManager.getTask(epic2.getId()));
    }

    @Test
    public void updateEpicTest() {
        String newName = "epic1 new name";
        epic1.setName(newName);
        String newDescription = "epic1 new description";
        epic1.setDescription(newDescription);
        taskManager.updateEpic(epic1);
        assertEquals(newName, taskManager.getTask(epic1.getId()).getName());
        assertEquals(newDescription, taskManager.getTask(epic1.getId()).getDescription());
    }

    @Test
    public void updateTaskTest() {
        String newName = "task1 new name";
        task1.setName(newName);
        String newDescription = "task1 new description";
        task1.setDescription(newDescription);
        taskManager.updateTask(task1);
        assertEquals(newName, taskManager.getTask(task1.getId()).getName());
        assertEquals(newDescription, taskManager.getTask(task1.getId()).getDescription());
    }

    @Test
    public void updateSubtaskTest() {
        String newName = "subtask1 new name";
        subtask1.setName(newName);
        String newDescription = "subtask1 new description";
        subtask1.setDescription(newDescription);
        taskManager.updateSubtask(subtask1);
        assertEquals(newName, taskManager.getTask(subtask1.getId()).getName());
        assertEquals(newDescription, taskManager.getTask(subtask1.getId()).getDescription());
    }

    @Test
    public void notConflictIdsTest() {
        Task task3 = new Task("task3", "desc3", 1);
        taskManager.createTask(task3);
        Task task4 = taskManager.getTask(task3.getId());
        assertEquals(task3, task4);
        assertEquals(task3.getStatus(), task4.getStatus());
        assertEquals(task3.getDescription(), task4.getDescription());
        assertEquals(task3.getName(), task4.getName());
    }
}
