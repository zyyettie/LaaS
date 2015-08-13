package org.g6.laas.server.database.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.event.Notifiable;
import org.g6.laas.server.database.event.NotificationListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Collection;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(NotificationListener.class)
public abstract class LaaSNotifiable<U> extends LaaSAuditable<U> implements Notifiable<U> {

    public LaaSNotifiable(Long id) {
        super(id);
    }

    public U receiveFrom() {
        return this.getCreatedBy();
    }

    public abstract Collection<U> sendTo();

    public abstract String getSummary();
}
