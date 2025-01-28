package ru.yandex.practicum.java_kanban.model;

public class Subtask extends Task {
    private Epic epic;

    @Override
    public String toCsv() {
        String csvString = super.toCsv();
        return csvString + "," + epic.getId();
    }

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        epic.addSubtask(this);
        epic.updateStatus();
    }

    public Subtask(String name, String description, Epic epic, long id) {
        this(name, description, epic);
        this.id = id;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epic = new Epic(subtask.epic);
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        epic.updateStatus();
    }

    public void setEpic(Epic epic) {
        this.epic.removeSubtask(this);
        this.epic.updateStatus();
        this.epic = epic;
        epic.addSubtask(this);
        epic.updateStatus();
    }

    public Epic getEpic() {
        return epic;
    }

    public void removeEpic() {
        epic.removeSubtask(this);
        epic.updateStatus();
        epic = null;
    }

    public void nullifyEpic() {
        epic = null;
    }

    @Override
    public String toString() {
        String result;
        if (epic == null) {
            result = "Subtask{" +
                    "epicId=" + null +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    '}';
        } else {
            result = "Subtask{" +
                    "epicId=" + epic.getId() +
                    ", name='" + name + '\'' +
                    ", description='" + description + '\'' +
                    ", status=" + status +
                    '}';
        }
        return result;
    }
}
