package ru.yandex.practicum.java_kanban;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.model.TaskStatus;
import ru.yandex.practicum.java_kanban.service.TaskManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MainTest {
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Subtask subtask3;
    private TaskManager taskManager;


    @BeforeEach
    public void setup() {
        taskManager = new TaskManager();
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
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
    }

    @Test
    @DisplayName("Get tasks list test")
    public void testTasksList() {
        assertEquals(2, taskManager.getTasks().size());
    }

    @Test
    @DisplayName("Get subtasks list test")
    public void testSubtasksList() {
        assertEquals(3, taskManager.getSubtasks().size());
    }

    @Test
    @DisplayName("Get epic tasks list test")
    public void testEpicsList() {
        assertEquals(2, taskManager.getEpics().size());
    }

    @Test
    @DisplayName("Update task status")
    public void updateTaskStatus() {
        task1.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStatus(TaskStatus.DONE);
        assertEquals(task1.getStatus(), TaskStatus.IN_PROGRESS);
        assertEquals(task2.getStatus(), TaskStatus.DONE);
    }

    @Test
    @DisplayName("Update subtasks status")
    public void updateSubtasksStatus() {
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
    @DisplayName("Remove tasks test")
    public void removeTasksTest() {
        taskManager.removeTasks();
        assertEquals(0, taskManager.getTasks().size());

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.DONE);
        assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS);

        taskManager.removeSubtask(subtask1.getId());
        assertNull(subtask1.getEpic());
        assertNull(epic1.getSubtask(subtask1.getId()));
        assertEquals(epic1.getStatus(), TaskStatus.DONE);

        taskManager.removeSubtask(subtask2.getId());
        assertNull(subtask2.getEpic());
        assertNull(epic1.getSubtask(subtask2.getId()));
        assertEquals(epic1.getStatus(), TaskStatus.NEW);

        taskManager.removeEpic(epic2.getId());
        assertNull(taskManager.getEpic(epic2.getId()));
    }

    @Test
    @DisplayName("Update epic test")
    public void updateEpicTest() {
        String newName = "epic1 new name";
        epic1.setName(newName);
        String newDescription = "epic1 new description";
        epic1.setDescription(newDescription);
        taskManager.updateEpic(epic1);
        assertEquals(newName, taskManager.getEpic(epic1.getId()).getName());
        assertEquals(newDescription, taskManager.getEpic(epic1.getId()).getDescription());
    }

    @Test
    @DisplayName("Update task test")
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
    @DisplayName("Update subtask test")
    public void updateSubtaskTest() {
        String newName = "subtask1 new name";
        subtask1.setName(newName);
        String newDescription = "subtask1 new description";
        subtask1.setDescription(newDescription);
        taskManager.updateTask(subtask1);
        assertEquals(newName, taskManager.getSubtask(subtask1.getId()).getName());
        assertEquals(newDescription, taskManager.getSubtask(subtask1.getId()).getDescription());
    }
}
