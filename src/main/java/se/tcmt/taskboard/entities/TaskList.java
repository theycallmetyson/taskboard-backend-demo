package se.tcmt.taskboard.entities;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "task_lists")
@NoArgsConstructor
@Schema(description = "Representation of a List")
public class TaskList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the list", example = "1")
    private Long id;

    @Schema(description = "Name of the task", example = "Sample List")
    private String name;

    @OneToMany(mappedBy = "taskListId", cascade = CascadeType.ALL, orphanRemoval = true)
    @ArraySchema(schema = @Schema(implementation = Task.class))
    @Schema(description = "List of tasks within this task list")
    private List<Task> tasks = new ArrayList<>();
}
