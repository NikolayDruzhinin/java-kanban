package ru.yandex.practicum.java_kanban.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.java_kanban.exception.IntersectionException;
import ru.yandex.practicum.java_kanban.exception.NotFoundException;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.server.type_tokens.TaskTypeToken;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public class HistoryHandler<T extends Task> extends BaseHttpHandler<T> implements HttpHandler {
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
    protected void processGet(HttpExchange httpExchange, String[] splitPath) throws IOException {
        List<T> history = historyManager.getHistory();
        sendText(httpExchange, gson.toJson(history, new TaskTypeToken().getType()));
    }

    @Override
    protected void processPost(HttpExchange exchange, String[] splitPath) throws NotFoundException, IntersectionException {

    }

}
