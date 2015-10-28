package org.g6.laas.server.database.repository;


import org.g6.laas.server.database.entity.JobRunning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IJobRunningRepository extends JpaRepository<JobRunning, Long> {
    @Query(value = "SELECT p FROM JobRunning p WHERE p.syn = :syn and p.status = :status")
    public List<JobRunning> findUnFinishedJobInQueue(@Param("syn")String syn, @Param("status")String status);
}
