package se.tcmt.taskboard.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.tcmt.taskboard.entities.Task;
import se.tcmt.taskboard.entities.TaskList;
import se.tcmt.taskboard.repositories.TaskListRepository;
import se.tcmt.taskboard.repositories.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Transactional
    public Task createTask(Long listId, Task task) {
        if (!taskListRepository.existsById(listId)) {
            throw new IllegalArgumentException("List not found");
        }
        task.setTaskListId(listId);

        return taskRepository.save(task);
    }


    @Transactional
    public Task updateTask(Long taskId, Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (updatedTask.getName() != null) {
            existingTask.setName(updatedTask.getName());
        }

        if (updatedTask.getDescription() != null) {
            existingTask.setDescription(updatedTask.getDescription());
        }

        if (updatedTask.getTaskListId() != null) {
            if (!taskListRepository.existsById(updatedTask.getTaskListId())) {
                throw new IllegalArgumentException("TaskList not found");
            }
            existingTask.setTaskListId(updatedTask.getTaskListId());
        }

        return taskRepository.save(existingTask);
    }


    @Transactional
    public boolean moveTask(Long sourceId, Long targetId) {
        Task task = taskRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        taskListRepository.findById(targetId).orElseThrow(() -> new IllegalArgumentException("List not found"));

        task.setTaskListId(targetId);
        taskRepository.save(task);

        return true;
    }

    @Transactional
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasksByTaskListId(Long id) {
        return taskRepository.findAllByTaskListId(id);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findAll().forEach(tasks::add);
        return tasks;
    }
}
