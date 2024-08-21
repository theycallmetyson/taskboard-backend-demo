package se.tcmt.taskboard.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import se.tcmt.taskboard.entities.Task;
import se.tcmt.taskboard.entities.TaskList;
import se.tcmt.taskboard.repositories.TaskListRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final TaskService taskService;

    public TaskListService(TaskListRepository taskListRepository, TaskService taskService) {
        this.taskListRepository = taskListRepository;
        this.taskService = taskService;
    }

    public List<TaskList> getAllTaskLists() {
        List<TaskList> taskLists = new ArrayList<>();
        taskListRepository.findAll().forEach(taskLists::add);
        for (TaskList taskList : taskLists) {
            List<Task> tasks = taskService.getAllTasksByTaskListId(taskList.getId());
            taskList.setTasks(tasks);
        }
        return taskLists;
    }

    public TaskList createTaskList(String name) {
        TaskList taskList = new TaskList();
        taskList.setName(name);
        return taskListRepository.save(taskList);
    }

    @Transactional(readOnly = true)
    public TaskList getTaskListWithTasks(Long taskListId) {
        TaskList taskList = taskListRepository.findById(taskListId).orElse(null);
        List<Task> tasks = taskService.getAllTasksByTaskListId(taskListId);
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        assert taskList != null;
        taskList.setTasks(tasks);
        return taskList;
    }

    public TaskList updateTaskList(Long id, String name) {
        TaskList taskList = taskListRepository.findById(id).orElse(null);
        if (taskList != null) {
            taskList.setName(name);
            return taskListRepository.save(taskList);
        }
        return null;
    }

    public boolean deleteTaskList(Long id) {
        if (taskListRepository.existsById(id)) {
            taskListRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
