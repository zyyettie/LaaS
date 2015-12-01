package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.task.ScenarioRunning;
import org.g6.laas.server.database.entity.user.Users;

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

    @OneToMany(cascade={CascadeType.ALL}, mappedBy = "jobRunning")
    private Collection<ScenarioRunning> scenarioRunnings = new ArrayList<>();

    @Override
    public Collection<Users> sendTo() {
        return getToUsers();
    }

    public void addUser(Users user){
        getToUsers().add(user);
    }

    public void addScenarioRunning(ScenarioRunning scenarioRunning){
        scenarioRunnings.add(scenarioRunning);
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
        if (this.getCreatedDate() == null)
            return "";
        return this.getCreatedDate().toString("yyyy/MM/dd HH:mm:ss");
    }
}
