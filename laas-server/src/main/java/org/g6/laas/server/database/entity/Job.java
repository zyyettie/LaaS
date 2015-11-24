package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "JOB", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class Job extends JobSubEntity {
    private static final long serialVersionUID = 4446780211212032935L;

    @Column(name = "NAME")
    private String name;

    private String description;

    @ManyToOne
    private Product product;

    @ManyToMany
    private List<Category> categories = new ArrayList<>();

    @ManyToMany
    private List<Scenario> scenarios = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    private List<JobRunning> jobRunnings = new ArrayList<>();

    @Override
    public Collection<User> sendTo() {
        return new ArrayList<>();
    }

    @Override
    public String getSummary() {
        return "";
    }

    public String getJobDate() {
        return this.getCreatedDate().toString("yyyy/MM/dd HH:mm:ss");
    }

    public String getScenario() {
        if (this.scenarios.size() > 0)
            return this.scenarios.get(0).getName();
        return "";
    }
}
