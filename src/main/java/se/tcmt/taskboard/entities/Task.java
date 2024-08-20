package se.tcmt.taskboard.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@Schema(description = "Representation of a Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the task", example = "1")
    private Long id;

    @Schema(description = "Name of the task", example = "Sample Task")
    private String name;

    @Schema(description = "Description of the task", example = "This is a sample task")
    private String description;

    @Schema(description = "ID of the task list to which this task belongs", example = "1")
    private Long taskListId;
}
