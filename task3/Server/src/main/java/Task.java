import java.time.LocalDateTime;

class Task {
    private LocalDateTime timeTask;
    private String taskText;

    Task(long seconds, String task, LocalDateTime timeTask) {
        this.timeTask = timeTask.plusSeconds(seconds);
        this.taskText = task;
    }

    LocalDateTime getTimeTask() {
        return timeTask;
    }

    String getTaskText() {
        return taskText;
    }
}
