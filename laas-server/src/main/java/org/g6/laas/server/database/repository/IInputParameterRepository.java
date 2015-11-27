package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.task.InputParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInputParameterRepository extends JpaRepository<InputParameter, Long> {
}
