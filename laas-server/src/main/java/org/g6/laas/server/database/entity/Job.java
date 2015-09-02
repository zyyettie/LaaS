package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.task.Workflow;
import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "JOB",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class Job extends LaaSAuditable<User> {
    private static final long serialVersionUID = 4446780211212032935L;

    @Column(name="NAME")
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @CreatedBy
    private User user;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "JOB_SCENARIO",
            inverseJoinColumns = @JoinColumn(name = "SCENARIO_ID"),
            joinColumns = @JoinColumn(name = "JOB_ID"))
    private Collection<Workflow> scenarios = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REFRESH)
    private Collection<File> files = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REFRESH)
    private Collection<JobResult> results = new ArrayList<>();
}
