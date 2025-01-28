package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.*;
import ru.yandex.practicum.java_kanban.util.DeserializationException;
import ru.yandex.practicum.java_kanban.util.ManagerSaveException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

public class FileBackedTaskManager<T extends Task> extends InMemoryTaskManager<T> {
    private final Path filename;
    private Logger logger = Logger.getLogger(FileBackedTaskManager.class.getName());


    public FileBackedTaskManager(Path filename) {
        super();
        this.filename = filename;
        if (Files.exists(filename)) {
            load();
        }
    }

    private void load() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename.toFile()))) {
            //read first line with table names
            br.readLine();
            String tmpLine;
            while ((tmpLine = br.readLine()) != null) {
                T task = deserializeObject(tmpLine);
                tasks.put(task.getId(), task);
                if (idCounter.get() >= task.getId()) {
                    idCounter.set(task.getId());
                }
            }
        } catch (Exception e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
    }

    private T deserializeObject(String line) {
        String[] data = line.split(",");
        long id = Long.parseLong(data[0]);
        TaskType taskType = TaskType.valueOf(data[1]);
        String name = data[2];
        TaskStatus taskStatus = TaskStatus.valueOf(data[3]);
        String description = data[4];
        T resultTusk = switch (taskType) {
            case SUBTASK: {
                Long epicId = Long.parseLong(data[5]);
                Epic epic = tasks.containsKey(epicId) ? (Epic) tasks.get(epicId) : new Epic(name, description, epicId);
                Subtask subtask = new Subtask(name, description, epic, id);
                yield (T) subtask;
            }
            case EPIC:
                yield (T) new Epic(name, description, id);

            case TASK: {
                yield (T) new Task(name, description, id);
            }
            default:
                throw new DeserializationException("Unknown task type: " + taskType);
        };
        resultTusk.setStatus(taskStatus);
        return resultTusk;
    }

    private void save() {
        try {
            if (!Files.exists(filename)) {
                Files.createFile(filename);
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename.toFile()))) {
                bw.write("id,type,name,status,description,epic");
                bw.newLine();
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Long, T> entry : tasks.entrySet()) {
                    sb.append(entry.getValue().toCsv());
                    sb.append("\n");
                }
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
    public void updateTask(Task t) {
        super.updateTask(t);
        save();
    }

    @Override
    public void updateSubtask(Subtask t) {
        super.updateSubtask(t);
        save();
    }

    @Override
    public void updateEpic(Epic t) {
        super.updateEpic(t);
        save();
    }
}
