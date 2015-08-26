package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import org.g6.laas.server.database.entity.task.Workflow;
import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.annotation.CreatedBy;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "SCENARIO",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Scenario extends LaaSAuditable<User> {
    private static final long serialVersionUID = -5373740423412420415L;

    @Column(name = "NAME")
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "SCENARIO_CATEGORY",
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID"),
            joinColumns = @JoinColumn(name = "SCENARIO_ID"))
    private Collection<Category> categories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @CreatedBy
    private User user;

    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "SCENARIO_WORKFLOW",
            inverseJoinColumns = @JoinColumn(name = "WORKFLOW_ID"),
            joinColumns = @JoinColumn(name = "SCENARIO_ID"))
    private Collection<Workflow> workflows = new ArrayList<>();

    @OneToMany(mappedBy = "scenario")
    private java.util.Collection<ScenarioHistory> scenarioHistory = new ArrayList<>();
}
