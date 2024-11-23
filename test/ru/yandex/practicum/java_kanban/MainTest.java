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
        taskManager = TaskManager.getInstance();
        taskManager.clearData();
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
    @DisplayName("Get tasks list test")
    public void testTasksList() {
        assertEquals(2, taskManager.getTasks(Task.class).size());
    }

    @Test
    @DisplayName("Get subtasks list test")
    public void testSubtasksList() {
        assertEquals(3, taskManager.getTasks(Subtask.class).size());
    }

    @Test
    @DisplayName("Get epic tasks list test")
    public void testEpicsList() {
        assertEquals(2, taskManager.getTasks(Epic.class).size());
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
        taskManager.removeTasks(Task.class);
        assertEquals(0, taskManager.getTasks(Task.class).size());

        taskManager.removeTasks(Subtask.class);
        assertEquals(0, taskManager.getTasks(Subtask.class).size());
        assertEquals(0, epic1.getSubtasks().size());
        assertEquals(0, epic2.getSubtasks().size());

        taskManager.removeById(epic1.getId());
        assertNull(taskManager.getTask(epic1.getId()));
        taskManager.removeById(epic2.getId());
        assertNull(taskManager.getTask(epic2.getId()));
    }
}
