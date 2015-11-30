package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;

@Entity
@Table
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class OrderedTask extends LaaSAuditable<User> implements Comparable<OrderedTask>{
    private static final long serialVersionUID = -7080540921514432085L;

    @ManyToOne
    private Scenario scenario;

    @ManyToOne
    private Task task;

    @Column(name="TASK_ORDER")
    private int order;

    @Override
    public int compareTo(OrderedTask o) {
        if(this.order < o.order)
            return -1;
        if(this.order > o.order)
            return 1;
        return 0;
    }
}
