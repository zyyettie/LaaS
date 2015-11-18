package org.g6.laas.server.database.entity.user.projection;

import org.g6.laas.server.database.entity.user.Notification;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "notificationsummary", types = {Notification.class})
public interface NotificationSummaryProjection {

    Long getId();

    UserSummaryProjection getFrom();

    String getSummary();

    String getStatus();
}
