package ru.yandex.practicum.java_kanban.server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.java_kanban.exception.IntersectionException;
import ru.yandex.practicum.java_kanban.exception.NotFoundException;
import ru.yandex.practicum.java_kanban.model.Epic;
import ru.yandex.practicum.java_kanban.model.Subtask;
import ru.yandex.practicum.java_kanban.server.type_tokens.SubtaskTypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler<Subtask> implements HttpHandler {
    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException, IntersectionException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (splitPath.length == 3) {
            Subtask subtask = gson.fromJson(requestBody, Subtask.class);
            inMemoryTaskManager.updateTask(subtask);
            historyManager.add(inMemoryTaskManager.getTask(Long.parseLong(splitPath[2])));
        } else {
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Subtask subtask = new Subtask(jsonObject.get("name").getAsString(),
                    jsonObject.get("description").getAsString(),
                    (Epic) inMemoryTaskManager.getTask(jsonObject.get("epicId").getAsLong()));

            inMemoryTaskManager.createTask(subtask);
            historyManager.add(subtask);
        }
        sendText(exchange);
    }

    @Override
    protected void processGet(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException {
        if (splitPath.length == 3) {
            Subtask subtask = (Subtask) inMemoryTaskManager.getTask(Long.parseLong(splitPath[2]));
            sendText(exchange, gson.toJson(subtask));
        } else {
            List<Subtask> subtasks = inMemoryTaskManager.getTasks();
            sendText(exchange, gson.toJson(subtasks, new SubtaskTypeToken().getType()));
        }
    }
}
