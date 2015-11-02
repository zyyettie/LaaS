package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFileRepository extends JpaRepository<File, Long> {

    @Query(value = "SELECT f FROM File f WHERE f.isRemoved <> 'Y' and f.createdBy.name = :userName")
    public List<File> findFilesOwnedBy(@Param("userName")String userName);
}
