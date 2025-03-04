package ru.yandex.practicum.java_kanban;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.java_kanban.server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Logger logger = Logger.getLogger(HttpTaskServer.class.getName());
    private static final HttpServer httpServer;

    static {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            logger.severe(e.getMessage());
            throw new RuntimeException(e);
        }
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/subtasks", new SubtasksHandler());
        httpServer.createContext("/epics", new EpicsHandler());
        httpServer.createContext("/history", new HistoryHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
    }

    public static void main(String[] args) {
        start();
    }

    public static void start() {
        httpServer.start();
        logger.info("HTTP-server is running on " + PORT);
    }

    public static void stop(int delay) {
        httpServer.stop(delay);
        logger.info("HTTP-server is stopped");
    }
}
