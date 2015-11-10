package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
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

    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy = "jobRunning")
    private Collection<TaskRunning> taskRunnings = new ArrayList<>();

    @Override
    public Collection<User> sendTo() {
        return getToUsers();
    }

    public void addUser(User user){
        getToUsers().add(user);
    }

    public void addTaskRunning(TaskRunning taskRunning){
        taskRunnings.add(taskRunning);
    }

    public String getJobName() {
        return this.job.getName();
    }

    public String getJobDescription() {
        return this.job.getDescription();
    }

    public String getUser() {
        return this.getCreatedBy().getName();
    }

    public String getRunningTime() {
        return this.getCreatedDate().toString("yyyy/MM/dd HH:mm:ss");
    }
}
