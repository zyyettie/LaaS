package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.user.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInboxRepository extends JpaRepository<Inbox, Long> {
}
