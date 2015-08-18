package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFileRepository extends JpaRepository<File, Long> {
}
