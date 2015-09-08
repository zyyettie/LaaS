package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;

@Entity
@Data
@Table
@EqualsAndHashCode(callSuper = false)
public class TaskRunning extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8603475553208415613L;

    @ManyToOne
    private Task task;

    @ManyToOne
    private JobRunning jobRunning;

    private String status;

    @OneToOne
    private TaskResult result;

    //million second
    private Long duration;
}
