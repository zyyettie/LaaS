package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkflowRepository extends JpaRepository<Workflow, Long> {
}
