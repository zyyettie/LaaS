package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "JOB",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class Job extends LaaSAuditable<User> {
    private static final long serialVersionUID = 4446780211212032935L;

    @Column(name="NAME")
    private String name;

    private String description;

    private String parameters;

    @ManyToOne
    private Product product;

    @ManyToMany
    private Collection<Category> categories = new ArrayList<>();

    @ManyToMany
    private Collection<Scenario> scenarios = new ArrayList<>();

    @OneToMany(mappedBy = "job")
    private Collection<JobRunning> jobRunnings = new ArrayList<>();

    @ManyToMany
    private Collection<File> files = new ArrayList<>();
}
