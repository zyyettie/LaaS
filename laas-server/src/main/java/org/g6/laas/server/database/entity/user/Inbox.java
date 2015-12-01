package org.g6.laas.server.database.entity.user;

import lombok.*;
import org.g6.laas.server.database.entity.LaaSPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INBOX")
public class Inbox extends LaaSPersistable {

    private static final long serialVersionUID = 8365840187902479233L;

    @NonNull
    @OneToOne(mappedBy = "inbox")
    private Users user;

    @OneToMany(mappedBy = "inbox")
    private Collection<Notification> notifications = new ArrayList<>();

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

}