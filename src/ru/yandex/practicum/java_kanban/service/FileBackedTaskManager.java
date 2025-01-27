package ru.yandex.practicum.java_kanban.service;

import ru.yandex.practicum.java_kanban.model.*;
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
            }
        } catch (Exception e) {
            throw new ManagerSaveException(e.getMessage(), e.getCause());
        }
    }

    public T deserializeObject(String line) {
        String[] data = line.split(",");
        long id = Long.parseLong(data[0]);
        TaskType taskType = TaskType.fromString(data[1]);
        String name = data[2];
        TaskStatus taskStatus = TaskStatus.fromString(data[3]);
        String description = data[4];
        T t;
        switch (taskType) {
            case SUBTASK -> {
                Long epicId = Long.parseLong(data[5]);
                Epic epic;
                if (tasks.containsKey(epicId)) {
                    epic = (Epic) tasks.get(epicId);
                } else {
                    epic = new Epic("epic", "description", epicId);
                }
                Subtask subtask = new Subtask(name, description, epic, id);
                t = (T) subtask;
            }
            case EPIC -> t = (T) new Epic(name, description, id);
            case TASK -> t = (T) new Task(name, description, id);
            default -> {
                return null;
            }
        }
        t.setStatus(taskStatus);
        return t;
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
                T tmp;
                for (Map.Entry<Long, T> entry : tasks.entrySet()) {
                    tmp = entry.getValue();
                    sb.append(entry.getKey());
                    sb.append(",");
                    sb.append(TaskType.fromString(tmp.getClass().getSimpleName()));
                    sb.append(",");
                    sb.append(tmp.getName());
                    sb.append(",");
                    sb.append(tmp.getStatus());
                    sb.append(",");
                    sb.append(tmp.getDescription());
                    if (tmp instanceof Subtask) {
                        sb.append(",");
                        sb.append(((Subtask) tmp).getEpic().getId());
                    }
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
