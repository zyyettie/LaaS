package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByName(@Param("name")String name);
}
