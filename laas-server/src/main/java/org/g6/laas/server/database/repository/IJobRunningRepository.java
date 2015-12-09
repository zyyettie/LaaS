package org.g6.laas.server.database.repository;


import org.g6.laas.server.database.entity.JobRunning;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobRunningRepository extends JpaRepository<JobRunning, Long> {
    @Query("SELECT p FROM JobRunning p WHERE p.syn = :syn and p.status = :status")
    public List<JobRunning> findUnFinishedJobInQueue(@Param("syn")String syn, @Param("status")String status);

    @Query("SELECT p FROM JobRunning p WHERE p.createdBy.name = :userName order by p.createdDate desc")
    public Page<JobRunning> findJobRunningsOwnedBy(@Param("userName")String userName, Pageable pageable);
}
