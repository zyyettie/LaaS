package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.result.JobResult;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class JobRunning extends LaaSAuditable<User> {
    private static final long serialVersionUID = -5742125355431226460L;

    @ManyToOne
    private Job job;

    private String status;

    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy = "jobRunning")
    private Collection<TaskRunning> taskRunnings = new ArrayList<>();

    @OneToOne
    private JobResult result;

}
