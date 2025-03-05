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
import ru.yandex.practicum.java_kanban.server.type_tokens.EpicTypeToken;
import ru.yandex.practicum.java_kanban.server.type_tokens.SubtaskTypeToken;
import ru.yandex.practicum.java_kanban.service.HistoryManager;
import ru.yandex.practicum.java_kanban.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler<Epic> implements HttpHandler {

    public EpicsHandler(TaskManager taskManager, HistoryManager historyManager) {
        super(taskManager, historyManager);
    }

    @Override
    protected void processGet(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException {
        Epic epic = (Epic) inMemoryTaskManager.getTask(Long.parseLong(splitPath[2]));
        if (splitPath.length == 4) {
            List<Subtask> subtasks = epic.getSubtasks();
            sendText(exchange, gson.toJson(subtasks, new SubtaskTypeToken().getType()));
        } else if (splitPath.length == 3) {
            sendText(exchange, gson.toJson(epic));
        } else {
            List<Epic> epics = inMemoryTaskManager.getEpics();
            sendText(exchange, gson.toJson(epics, new EpicTypeToken().getType()));
        }
    }

    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException,
            IntersectionException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (splitPath.length == 3) {
            Epic epic = gson.fromJson(requestBody, Epic.class);
            inMemoryTaskManager.updateTask(epic);
            historyManager.add(inMemoryTaskManager.getTask(Long.parseLong(splitPath[2])));
        } else {
            JsonElement jsonElement = JsonParser.parseString(requestBody);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Epic epic = new Epic(jsonObject.get("name").getAsString(), jsonObject.get("description").getAsString());

            inMemoryTaskManager.createTask(epic);
            historyManager.add(epic);
        }
        sendText(exchange);
    }

}
