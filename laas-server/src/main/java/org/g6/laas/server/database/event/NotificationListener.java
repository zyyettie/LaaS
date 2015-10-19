package org.g6.laas.server.database.event;

import org.g6.laas.server.database.entity.user.Inbox;
import org.g6.laas.server.database.entity.user.Notification;
import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.database.repository.INotificationRepository;
import org.g6.util.AutowireInjector;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PostPersist;
import java.util.Collection;

public class NotificationListener {

    @Autowired
    private INotificationRepository notificationRepository;

    @PostPersist
    public void sendNotification(Notifiable<User> entity) {
        User from = entity.receiveFrom();
        Collection<User> tos = entity.sendTo();
        String summary = entity.getSummary();
        for (User to : tos) {
            Inbox inbox = to.getInbox();
            Notification notification = new Notification(inbox, from, summary);
            if (this.notificationRepository == null) {
                AutowireInjector.inject(this, this.notificationRepository);
            }
            notificationRepository.save(notification);
        }
    }
}
