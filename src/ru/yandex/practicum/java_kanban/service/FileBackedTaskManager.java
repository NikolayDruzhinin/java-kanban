package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.*;
import ru.yandex.practicum.java_kanban.util.DeserializationException;
import ru.yandex.practicum.java_kanban.util.IntersectionException;
import ru.yandex.practicum.java_kanban.util.ManagerSaveException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTaskManager<T extends Task> extends InMemoryTaskManager<T> {
    private final Path filename;

    public FileBackedTaskManager(Path filename) {
        super();
        this.filename = filename;
        if (Files.exists(filename)) {
            try {
                load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void load() throws IOException, ManagerSaveException, IntersectionException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename.toFile()))) {
            br.readLine();
            String tmpLine;
            while ((tmpLine = br.readLine()) != null) {
                T task = deserializeObject(tmpLine);
                tasks.put(task.getId(), task);
                if (idCounter.get() >= task.getId()) {
                    idCounter.set(task.getId());
                }
            }
            tasks.values().forEach(task -> updateTask(task));
        }
    }

    private T deserializeObject(String line) {
        String[] data = line.split(",");
        long id = Long.parseLong(data[0]);
        TaskType taskType = TaskType.valueOf(data[1]);
        String name = data[2];
        TaskStatus taskStatus = TaskStatus.valueOf(data[3]);
        String description = data[4];
        LocalDateTime startTime = LocalDateTime.parse(data[5]);
        T resultTusk = switch (taskType) {
            case SUBTASK: {
                Long epicId = Long.parseLong(data[data.length - 1]);
                Epic epic = (Epic) tasks.get(epicId);
                Subtask subtask = new Subtask(name, description, epic);
                subtask.setId(id);
                if (epic != null) {
                    epic.addSubtask(subtask);
                }
                yield (T) subtask;
            }
            case EPIC:
                Epic epic = new Epic(name, description);
                epic.setId(id);
                yield (T) epic;
            case TASK:
                Task task = new Task(name, description);
                task.setId(id);
                yield (T) task;
            default:
                throw new DeserializationException("Unknown task type: " + taskType);
        };
        resultTusk.setStartTime(startTime);
        resultTusk.setStatus(taskStatus);
        if (taskStatus.equals(TaskStatus.DONE)) {
            resultTusk.setDuration(Duration.ofMinutes(Long.parseLong(data[6])));
        }

        return resultTusk;
    }

    private void save() {
        try {
            if (!Files.exists(filename)) {
                Files.createFile(filename);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename.toFile()))) {
                bw.write("id,type,name,status,description,startTime,duration,epic");
                bw.newLine();
                StringBuilder sb = new StringBuilder();
                tasks.entrySet().forEach(entry -> {
                    sb.append(entry.getValue().toCsv());
                    sb.append("\n");
                });
                bw.write(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeTask(T task) {
        super.removeTask(task);
        save();
    }

    @Override
    public void createTask(T task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(T t) {
        super.updateTask(t);
        save();
    }
}
