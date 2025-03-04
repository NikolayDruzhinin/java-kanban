package ru.yandex.practicum.java_kanban.server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.java_kanban.exception.IntersectionException;
import ru.yandex.practicum.java_kanban.exception.NotFoundException;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.server.type_tokens.TaskTypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler<Task> implements HttpHandler {

    @Override
    protected void processGet(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException {
        if (splitPath.length == 3) {
            Task task = inMemoryTaskManager.getTask(Long.parseLong(splitPath[2]));
            sendText(exchange, gson.toJson(task));
        } else {
            List<Task> tasks = inMemoryTaskManager.getTasks();
            sendText(exchange, gson.toJson(tasks, new TaskTypeToken().getType()));
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException,
            IntersectionException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (splitPath.length == 3) {
            Task task = gson.fromJson(requestBody, Task.class);
            inMemoryTaskManager.updateTask(task);
            historyManager.add(inMemoryTaskManager.getTask(Long.parseLong(splitPath[2])));
        } else {
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Task task = new Task(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString());
            inMemoryTaskManager.createTask(task);
            historyManager.add(task);
        }
        sendText(exchange);
    }


}
