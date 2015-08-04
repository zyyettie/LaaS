package org.g6.laas.server.database.event;

import org.g6.laas.server.database.entity.user.User;

import javax.persistence.PostPersist;
import java.util.Collection;

public class NotificationListener {


    @PostPersist
    public void sendNotification(Notifiable<User> entity) {
        User from = entity.receiveFrom();
        Collection<User> tos = entity.sendTo();
        String summary = entity.getSummary();
    }
}
