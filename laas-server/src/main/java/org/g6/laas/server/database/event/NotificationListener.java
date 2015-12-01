package org.g6.laas.server.database.event;

import org.g6.laas.server.database.entity.user.Inbox;
import org.g6.laas.server.database.entity.user.Notification;
import org.g6.laas.server.database.entity.user.Users;
import org.g6.laas.server.database.repository.INotificationRepository;
import org.g6.laas.util.AutowireInjector;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.util.Collection;

public class NotificationListener {

    @Autowired
    private INotificationRepository notificationRepository;

    @PostPersist
    @PostUpdate
    public void sendNotification(Notifiable<Users> entity) {
        Users from = entity.receiveFrom();
        Collection<Users> tos = entity.sendTo();
        String summary = entity.getSummary();
        for (Users to : tos) {
            Inbox inbox = to.getInbox();
            Notification notification = new Notification(inbox, from, summary);
            if (this.notificationRepository == null) {
                AutowireInjector.inject(this, this.notificationRepository);
            }
            notificationRepository.save(notification);
        }
    }
}
