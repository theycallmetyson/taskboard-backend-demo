package se.tcmt.taskboard.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.tcmt.taskboard.entities.TaskList;
import se.tcmt.taskboard.exceptions.NotFoundException;
import se.tcmt.taskboard.services.TaskListService;

import java.util.List;

@RestController
@RequestMapping(path = "/lists", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskListController {

    private static final String LIST_NOT_FOUND_MESSAGE = "List not found";

    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping
    public ResponseEntity<List<TaskList>> getAllTaskLists() {
        List<TaskList> taskLists = taskListService.getAllTaskLists();
        if (taskLists.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskList> getTaskList(@PathVariable Long id) {
        TaskList taskList = taskListService.getTaskListWithTasks(id);
        if (taskList == null) {
            throw new NotFoundException(LIST_NOT_FOUND_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }


    @PostMapping
    public ResponseEntity<TaskList> createTaskList(@RequestParam String name) {
        TaskList taskList = taskListService.createTaskList(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskList> updateTaskList(@PathVariable Long id, @RequestParam String name) {
        TaskList updatedTaskList = taskListService.updateTaskList(id, name);
        if (updatedTaskList == null) {
            throw new NotFoundException(LIST_NOT_FOUND_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.OK).body(updatedTaskList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable Long id) {
        boolean deleted = taskListService.deleteTaskList(id);
        if (!deleted) {
            throw new NotFoundException(LIST_NOT_FOUND_MESSAGE);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
