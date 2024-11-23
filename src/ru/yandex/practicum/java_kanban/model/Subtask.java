package ru.yandex.practicum.java_kanban.model;

public class Subtask extends Task{
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        epic.addSubtask(this);
    }

    @Override
    public void setStatus(TaskStatus status) {
        super.setStatus(status);
        epic.updateStatus();
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
        epic.addSubtask(this);
        epic.updateStatus();
    }

    public void removeSubtaskFromEpic() {
        epic.removeSubtask(this);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epic.getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
