package ru.yandex.practicum.java_kanban;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.java_kanban.server.handlers.*;
import ru.yandex.practicum.java_kanban.service.HistoryManager;
import ru.yandex.practicum.java_kanban.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class HttpTaskServer {
    private static final Logger logger = Logger.getLogger(HttpTaskServer.class.getName());
    private final HttpServer httpServer;
    private final int port;

    public HttpTaskServer(int port, TaskManager taskManager, HistoryManager historyManager) {
        this.port = port;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
        httpServer.createContext("/tasks", new TasksHandler(taskManager, historyManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager, historyManager));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, historyManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager, historyManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, historyManager));
    }

    public static void main(String[] args) {
    }

    public void start() {
        httpServer.start();
        logger.info("HTTP-server is running on port " + port);
    }

    public void stop(int delay) {
        httpServer.stop(delay);
        logger.info("HTTP-server is stopped");
    }
}
