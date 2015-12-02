package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobRepository  extends JpaRepository<Job, Long> {
    @Query("SELECT j FROM Job j WHERE j.createdBy.name = :userName order by j.createdDate desc")
    public Page<Job> findJobsOwnedBy(@Param("userName")String userName, Pageable pageable);
}
