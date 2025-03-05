package ru.yandex.practicum.java_kanban.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.java_kanban.exception.NotFoundException;
import ru.yandex.practicum.java_kanban.exception.NotImplementedException;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.server.type_tokens.TaskTypeToken;
import ru.yandex.practicum.java_kanban.service.HistoryManager;
import ru.yandex.practicum.java_kanban.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class PrioritizedHandler<T extends Task> extends BaseHttpHandler<T> implements HttpHandler {
    public PrioritizedHandler(TaskManager taskManager, HistoryManager historyManager) {
        super(taskManager, historyManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] splitPath = path.split("/");
        if (exchange.getRequestMethod().equals("GET")) {
            processGet(exchange, splitPath);
        }
    }

    @Override
    protected void processGet(HttpExchange httpExchange, String[] splitPath) throws IOException, NotFoundException {
        List<T> prioritizedTasks = inMemoryTaskManager.getPrioritizedTasks();
        sendText(httpExchange, gson.toJson(prioritizedTasks, new TaskTypeToken().getType()));
    }

    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath) throws NotImplementedException {
        throw new NotImplementedException("POST method not implemented");
    }
}
