package se.tcmt.taskboard.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.tcmt.taskboard.entities.TaskList;

@Repository
public interface TaskListRepository extends CrudRepository<TaskList, Long> {
}
