package org.g6.laas.server.database.entity.task;

import lombok.Data;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "TASK_HISTORY")
public class TaskHistory extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8603475553208415613L;

    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;

    private Date duration;
}
