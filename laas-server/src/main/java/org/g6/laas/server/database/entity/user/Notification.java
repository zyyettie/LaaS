package org.g6.laas.server.database.entity.user;

import lombok.*;
import org.g6.laas.server.database.entity.LaaSAuditable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "NOTIFICATION")
public class Notification extends LaaSAuditable<User> {

    private static final long serialVersionUID = -7457760948182175014L;

    @NonNull
    @ManyToOne
    private Inbox inbox;

    @NonNull
    @ManyToOne
    private User from;

    @NonNull
    private String summary;

    private String status = "NEW";
}
