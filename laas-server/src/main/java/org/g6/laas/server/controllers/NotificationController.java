package org.g6.laas.server.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.server.database.entity.user.Notification;
import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.database.repository.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NotificationController {

    @Autowired
    private INotificationRepository notificationRepository;

    @RequestMapping(value = "/controllers/notifications/{id}",method = RequestMethod.GET)
    @JsonView(Notification.NotificationSummary.class)
    public Notification readNotification(@PathVariable Long id){
        Notification reading = notificationRepository.findOne(id);
        if(reading != null){
            if(reading.getStatus().equals("NEW")||reading.getStatus() == null)
            reading.setStatus("READ");
            reading = notificationRepository.save(reading);
        }
        return reading;
    }
}
