package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.ScenarioHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IScenarioHistoryRepository extends JpaRepository<ScenarioHistory, Long> {
}
