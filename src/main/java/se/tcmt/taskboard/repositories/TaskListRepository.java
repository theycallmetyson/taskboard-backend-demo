package se.tcmt.taskboard.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.tcmt.taskboard.entities.TaskList;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}
