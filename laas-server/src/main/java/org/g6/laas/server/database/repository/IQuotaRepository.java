package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.user.Quota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuotaRepository extends JpaRepository<Quota, Long> {
    @Query("SELECT q FROM Quota q WHERE q.user.name = :userName")
    public Quota findUserQuota(@Param("userName")String userName);
}
