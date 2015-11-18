package org.g6.laas.server.database.repository;

import org.g6.laas.server.database.entity.user.Notification;
import org.g6.laas.server.database.entity.user.projection.NotificationSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = NotificationSummaryProjection.class)
public interface INotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.inbox = ?#{principal.inbox} and n.status=:status")
    Page<Notification> findMyNotification(@Param("status") String status, Pageable pageable);

    @Query("select n from Notification n where n.inbox = ?#{principal.inbox}")
    Page<Notification> allMyNotifications(Pageable pageable);

    Page<Notification> findByStatus(String status, Pageable pageable);

    @Query("select count(n) from Notification n where n.inbox = ?#{principal.inbox} and (n.status is null or n.status='NEW')")
    Long findMyUnreadCount();

}
