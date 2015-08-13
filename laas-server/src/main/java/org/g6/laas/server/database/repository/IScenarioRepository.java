package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IScenarioRepository extends JpaRepository<Scenario, Long> {
}
