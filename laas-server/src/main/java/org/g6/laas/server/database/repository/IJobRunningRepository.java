package org.g6.laas.server.database.repository;


import org.g6.laas.server.database.entity.JobRunning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IJobRunningRepository extends JpaRepository<JobRunning, Long> {
}
