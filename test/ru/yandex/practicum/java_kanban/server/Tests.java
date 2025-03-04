package ru.yandex.practicum.java_kanban.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.java_kanban.HttpTaskServer;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.server.adapters.DurationTypeAdapter;
import ru.yandex.practicum.java_kanban.server.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.java_kanban.server.type_tokens.EpicTypeToken;
import ru.yandex.practicum.java_kanban.server.type_tokens.SubtaskTypeToken;
import ru.yandex.practicum.java_kanban.server.type_tokens.TaskTypeToken;
import ru.yandex.practicum.java_kanban.service.HistoryManager;
import ru.yandex.practicum.java_kanban.service.TaskManager;
import ru.yandex.practicum.java_kanban.util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests<T extends Task> {
    protected final TaskManager inMemoryTaskManager = Managers.getDefaultInMemoryManager();
    protected final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .setPrettyPrinting()
            .create();

    @BeforeEach
    protected void clear() {
        inMemoryTaskManager.clear();
        historyManager.clear();
        HttpTaskServer.start();
    }

    @AfterEach
    protected void stop() {
        HttpTaskServer.stop();
    }

    public void createTest(T t) {
        assertEquals(201, createTaskRq(t));
    }

    public void getTest(T t) {
        createTaskRq(t);
        HttpResponse<String> response = getTaskRq(1);
        t = (T) gson.fromJson(response.body(), t.getClass());
        assertEquals(1, t.getId());
    }

    public void deleteTest(T t) {
        createTaskRq(t);
        assertEquals(201, deleteTaskRq(1));
        assertEquals(404, getTaskRq(1).statusCode());
    }

    public void getTest(T t1, T t2, T t3) {
        createTaskRq(t1);
        createTaskRq(t2);
        createTaskRq(t3);

        HttpResponse<String> response = getTasksRq();
        List<T> tasks;
        if (t1.getClass() == Task.class) {
            tasks = gson.fromJson(response.body(), new TaskTypeToken().getType());
        } else if (t1.getClass() == Epic.class) {
            tasks = gson.fromJson(response.body(), new EpicTypeToken().getType());
        } else {
            tasks = gson.fromJson(response.body(), new SubtaskTypeToken().getType());
        }
        assertEquals(1, tasks.get(0).getId());
        assertEquals(2, tasks.get(1).getId());
        assertEquals(3, tasks.get(2).getId());
    }

    public void updateTest(T t) {
        createTaskRq(t);
        HttpResponse<String> response = getTaskRq(1);
        t = (T) gson.fromJson(response.body(), t.getClass());
        String newTaskName = "New Task Name";
        t.setName(newTaskName);
        updateTaskRq(t);

        response = getTaskRq(1);
        t = (T) gson.fromJson(response.body(), t.getClass());
        assertEquals(newTaskName, t.getName());
    }

    protected int createTaskRq(T task) {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = createPostRequest(uri, gson.toJson(task));
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected int updateTaskRq(T task) {
        URI uri = URI.create("http://localhost:8080/tasks/" + task.getId());
        HttpRequest request = createPostRequest(uri, gson.toJson(task));
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpResponse<String> getTaskRq(long id) {
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = createGetRequest(uri);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected HttpResponse<String> getTasksRq() {
        URI uri = URI.create("http://localhost:8080/tasks");
        HttpRequest request = createGetRequest(uri);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected int deleteTaskRq(long id) {
        URI uri = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = createDeleteRequest(uri);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected static final HttpRequest createDeleteRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
    }

    protected static final HttpRequest createPostRequest(URI uri, String body) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    protected static final HttpRequest createGetRequest(URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json")
                .GET()
                .build();
    }

}
