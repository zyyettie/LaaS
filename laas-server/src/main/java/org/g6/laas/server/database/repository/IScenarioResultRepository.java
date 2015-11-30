package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.result.ScenarioResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IScenarioResultRepository extends JpaRepository<ScenarioResult, Long> {
}
