package org.g6.laas.server.database.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.event.Notifiable;
import org.g6.laas.server.database.event.NotificationListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@EntityListeners(NotificationListener.class)
public abstract class LaaSNotifiable<U> extends LaaSAuditable<U> implements Notifiable<U> {
    private String summary;

    public LaaSNotifiable(Long id) {
        super(id);
    }

    public U receiveFrom() {
        return this.getCreatedBy();
    }

    @ManyToMany(cascade={CascadeType.PERSIST})
    private List<U> toUsers = new ArrayList<>();
}
