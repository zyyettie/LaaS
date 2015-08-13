package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {
}
