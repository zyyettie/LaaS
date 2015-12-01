package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
    Users findByName(@Param("name")String name);
}
