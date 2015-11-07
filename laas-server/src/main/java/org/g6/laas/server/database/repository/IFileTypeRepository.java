package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.file.FileSummaryProjection;
import org.g6.laas.server.database.entity.file.FileType;
import org.g6.laas.server.database.entity.user.projection.NotificationSummaryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFileTypeRepository extends JpaRepository<FileType, Long> {
}

