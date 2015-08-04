package org.g6.laas.database.repository;

import org.g6.laas.database.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskHistoryRepository extends JpaRepository<TaskHistory, Integer> {
}
