package se.tcmt.taskboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.tcmt.taskboard.controllers.TaskController;
import se.tcmt.taskboard.entities.Task;
import se.tcmt.taskboard.exceptions.NotFoundException;
import se.tcmt.taskboard.services.TaskService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testGetAllTasks_EmptyList_ReturnsNoContent() throws Exception {
        when(taskService.getAllTasks()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllTasks_NonEmptyList_ReturnsOk() throws Exception {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setName("Test Task");
        tasks.add(task);

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    @Test
    void testGetAllTasks_ServiceReturnsNull_ReturnsInternalServerError() throws Exception {
        when(taskService.getAllTasks()).thenReturn(null);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateTask_ValidData_ReturnsCreated() throws Exception {
        Task task = new Task();
        task.setName("New Task");

        when(taskService.createTask(anyLong(), any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                        .param("listId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Task\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New Task"));
    }

    @Test
    void testCreateTask_InvalidListId_ReturnsNotFound() throws Exception {
        when(taskService.createTask(anyLong(), any(Task.class)))
                .thenThrow(new NotFoundException("List not found"));

        mockMvc.perform(post("/tasks")
                        .param("listId", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Task\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateTask_ValidData_ReturnsOk() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setName("Updated Task");

        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Task\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Task"));
    }

    @Test
    void testUpdateTask_TaskNotFound_ReturnsNotFound() throws Exception {
        when(taskService.updateTask(anyLong(), any(Task.class)))
                .thenThrow(new NotFoundException("Task not found"));

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Task\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testMoveTask_ValidIds_ReturnsOk() throws Exception {
        when(taskService.moveTask(anyLong(), anyLong())).thenReturn(true);

        mockMvc.perform(put("/tasks/move/1/2"))
                .andExpect(status().isOk());
    }

    @Test
    void testMoveTask_TaskOrTargetListNotFound_ReturnsNotFound() throws Exception {
        when(taskService.moveTask(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Task or target list not found"));

        mockMvc.perform(put("/tasks/move/1/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTask_ValidId_ReturnsNoContent() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn(true);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }

    @Test
     void testDeleteTask_TaskNotFound_ReturnsNotFound() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn(false);

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNotFound());
    }
}
