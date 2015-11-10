package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.file.FileSummaryProjection;
import org.g6.laas.server.database.entity.user.projection.NotificationSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(excerptProjection = FileSummaryProjection.class)
public interface IFileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT f FROM File f WHERE f.isRemoved <> 'Y' and f.createdBy.name = :userName order by f.createdDate desc")
    public Page<File> findFilesOwnedBy(@Param("userName")String userName, Pageable pageable);
}
