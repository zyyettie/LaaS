package org.g6.laas.database.repository;

import org.g6.laas.database.entity.FileLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileLocationRepository extends JpaRepository<FileLocation, Integer> {
}
