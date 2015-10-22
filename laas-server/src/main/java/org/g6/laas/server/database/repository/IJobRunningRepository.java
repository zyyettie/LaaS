package org.g6.laas.server.database.repository;


import org.g6.laas.server.database.entity.JobRunning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IJobRunningRepository extends JpaRepository<JobRunning, Long> {
    @Query(value = "SELECT p FROM JobRunning p WHERE p.syn = ?1 and p.status = ?2")
    public List<JobRunning> findUnFinishedJobInQueue(String syn, String status);
}
