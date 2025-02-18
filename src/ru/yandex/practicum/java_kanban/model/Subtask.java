package ru.yandex.practicum.java_kanban.model;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
    }

    public void setEpic(Epic epic) {
        this.epic.removeSubtask(this);
        this.epic.updateStatus();
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void removeEpic() {
        epic = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Subtask{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", description='").append(description).append('\'')
                .append(", status=").append(status);

        if (startTime != null) {
            sb.append(", startTime=").append(startTime).append('\'');
        }
        if (duration != null) {
            sb.append(", duration=").append(duration).append('\'');
        }
        if (epic != null) {
            sb.append(", epicId=").append(epic.getId()).append('\'');
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public String toCsv() {
        String csvString = super.toCsv();
        return csvString + "," + (epic == null ? 0 : epic.getId());
    }
}
