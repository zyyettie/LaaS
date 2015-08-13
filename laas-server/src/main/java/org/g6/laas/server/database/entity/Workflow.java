package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Workflow extends LaaSAuditable<User> {
    private static final long serialVersionUID = 3937898097604595839L;

    @Column(name="NAME")
    private String name;

    private String description;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "workflows")
    private Collection<Scenario> scenarios = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "WORKFLOW_TASK",
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"),
            joinColumns = @JoinColumn(name = "WORKFLOW_ID"))
    private Collection<Task> tasks = new ArrayList<>();
}
