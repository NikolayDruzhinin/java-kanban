package ru.yandex.practicum.java_kanban.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.java_kanban.exception.IntersectionException;
import ru.yandex.practicum.java_kanban.exception.NotFoundException;
import ru.yandex.practicum.java_kanban.model.Task;
import ru.yandex.practicum.java_kanban.server.adapters.DurationTypeAdapter;
import ru.yandex.practicum.java_kanban.server.adapters.LocalDateTimeAdapter;
import ru.yandex.practicum.java_kanban.service.HistoryManager;
import ru.yandex.practicum.java_kanban.service.TaskManager;
import ru.yandex.practicum.java_kanban.util.Managers;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler<T extends Task> implements HttpHandler {
    protected static final TaskManager inMemoryTaskManager = Managers.getDefaultInMemoryManager();
    protected static final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected Gson gson;

    public BaseHttpHandler() {
        super();
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendText(HttpExchange exchange) {
        try {
            exchange.sendResponseHeaders(201, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            exchange.close();
        }

    }

    protected void sendNotFound(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(404, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }

    }

    protected void sendHasInteractions(HttpExchange httpExchange) {
        try {
            httpExchange.sendResponseHeaders(406, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpExchange.close();
        }

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();
        String[] splitPath = path.split("/");
        try {
            switch (exchange.getRequestMethod()) {
                case "GET":
                    processGet(exchange, splitPath);
                    break;
                case "POST":
                    processPost(exchange, splitPath);
                    break;
                case "DELETE":
                    inMemoryTaskManager.removeTask(Long.parseLong(splitPath[2]));
                    historyManager.remove(Long.parseLong(splitPath[2]));
                    sendText(exchange);
                    break;
            }
        } catch (NotFoundException | NumberFormatException e) {
            e.printStackTrace();
            sendNotFound(exchange);
        } catch (IntersectionException e) {
            e.printStackTrace();
            sendHasInteractions(exchange);
        }
    }

    protected abstract void processGet(HttpExchange httpExchange, String[] splitPath) throws IOException, NotFoundException;

    protected abstract void processPost(HttpExchange exchange, String[] splitPath) throws IOException, NotFoundException, IntersectionException;
}
