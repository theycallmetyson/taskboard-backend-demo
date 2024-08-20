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
import se.tcmt.taskboard.controllers.TaskListController;
import se.tcmt.taskboard.entities.TaskList;
import se.tcmt.taskboard.services.TaskListService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskListControllerTest {

    @Mock
    private TaskListService taskListService;

    @InjectMocks
    private TaskListController taskListController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskListController).build();
    }

    @Test
    void testGetAllTaskLists_EmptyList_ReturnsNoContent() throws Exception {
        when(taskListService.getAllTaskLists()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/lists"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllTaskLists_NonEmptyList_ReturnsOk() throws Exception {
        List<TaskList> taskLists = new ArrayList<>();
        TaskList taskList = new TaskList();
        taskList.setName("Test TaskList");
        taskLists.add(taskList);

        when(taskListService.getAllTaskLists()).thenReturn(taskLists);

        mockMvc.perform(get("/lists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Test TaskList"));
    }

    @Test
    void testGetTaskList_ValidId_ReturnsOk() throws Exception {
        TaskList taskList = new TaskList();
        taskList.setName("Test TaskList");

        when(taskListService.getTaskListWithTasks(1L)).thenReturn(taskList);

        mockMvc.perform(get("/lists/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test TaskList"));
    }

    @Test
    void testGetTaskList_InvalidId_ReturnsNotFound() throws Exception {
        when(taskListService.getTaskListWithTasks(1L)).thenReturn(null);

        mockMvc.perform(get("/lists/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTaskList_ValidData_ReturnsCreated() throws Exception {
        TaskList taskList = new TaskList();
        taskList.setName("New TaskList");

        when(taskListService.createTaskList("New TaskList")).thenReturn(taskList);

        mockMvc.perform(post("/lists")
                        .param("name", "New TaskList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New TaskList"));
    }

    @Test
    void testUpdateTaskList_ValidId_ReturnsOk() throws Exception {
        TaskList updatedTaskList = new TaskList();
        updatedTaskList.setName("Updated TaskList");

        when(taskListService.updateTaskList(1L, "Updated TaskList")).thenReturn(updatedTaskList);

        mockMvc.perform(put("/lists/1")
                        .param("name", "Updated TaskList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated TaskList"));
    }

    @Test
    void testUpdateTaskList_InvalidId_ReturnsNotFound() throws Exception {
        when(taskListService.updateTaskList(1L, "Updated TaskList")).thenReturn(null);

        mockMvc.perform(put("/lists/1")
                        .param("name", "Updated TaskList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTaskList_ValidId_ReturnsNoContent() throws Exception {
        when(taskListService.deleteTaskList(1L)).thenReturn(true);

        mockMvc.perform(delete("/lists/1"))
                .andExpect(status().isNoContent());

        verify(taskListService).deleteTaskList(1L);
    }

    @Test
    void testDeleteTaskList_InvalidId_ReturnsNotFound() throws Exception {
        when(taskListService.deleteTaskList(1L)).thenReturn(false);

        mockMvc.perform(delete("/lists/1"))
                .andExpect(status().isNotFound());
    }
}
