package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NoArgsConstructor
@Data
public class JobRunning extends JobSubEntity {
    private static final long serialVersionUID = -5742125355431226460L;

    @ManyToOne
    private Job job;

    private String status;

    private String syn;//Y and N

    @OneToMany(mappedBy = "jobRunning")
    private Collection<TaskRunning> taskRunnings = new ArrayList<>();

    @Override
    public Collection<User> sendTo() {
        return new ArrayList<>();
    }

    @Override
    public String getSummary() {
        return "";
    }

    public void addTaskRunning(TaskRunning taskRunning){
        taskRunnings.add(taskRunning);
    }
}
