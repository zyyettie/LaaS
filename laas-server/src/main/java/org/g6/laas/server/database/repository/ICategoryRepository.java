package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
