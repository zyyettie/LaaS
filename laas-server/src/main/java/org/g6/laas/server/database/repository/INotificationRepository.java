package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.user.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface INotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.inbox = ?#{principal.inbox}")
    Page<Notification> findMyNotification(Pageable pageable);

}
