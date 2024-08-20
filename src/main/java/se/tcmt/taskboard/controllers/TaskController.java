package se.tcmt.taskboard.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.tcmt.taskboard.entities.Task;
import se.tcmt.taskboard.exceptions.NotFoundException;
import se.tcmt.taskboard.exceptions.ServiceException;
import se.tcmt.taskboard.services.TaskService;

import java.util.List;

@RestController
@RequestMapping(path = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found";

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();

        if (tasks == null) {
            throw new ServiceException("Could not get tasks");
        } else if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }


    @PostMapping
    public ResponseEntity<Task> createTask(@RequestParam Long listId, @RequestBody Task task) {
        Task createdTask = taskService.createTask(listId, task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task updatedTask) {
        Task task = taskService.updateTask(id, updatedTask);
        if (task == null) {
            throw new NotFoundException(TASK_NOT_FOUND_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/move/{sourceId}/{targetId}")
    public ResponseEntity<Void> moveTask(@PathVariable Long sourceId, @PathVariable Long targetId) {
        boolean moved = taskService.moveTask(sourceId, targetId);
        if (moved) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        throw new ServiceException("Could not move task");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        throw new NotFoundException(TASK_NOT_FOUND_MESSAGE);
    }
}
