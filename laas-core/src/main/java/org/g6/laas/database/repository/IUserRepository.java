package org.g6.laas.database.repository;

import org.g6.laas.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IUserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE LOWER(u.name)=LOWER(:name)")
    public User find(@Param("name")String name);
}
