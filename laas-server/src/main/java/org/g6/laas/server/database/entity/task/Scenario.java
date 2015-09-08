package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import org.g6.laas.server.database.entity.Category;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.Product;
import org.g6.laas.server.database.entity.user.User;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "SCENARIO",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Scenario extends LaaSAuditable<User> {
    private static final long serialVersionUID = -5373740423412420415L;

    @Column(name = "NAME")
    private String name;

    private String description;

    @ManyToOne
    private Product product;

    @ManyToMany
    private Collection<Category> categories = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "SCENARIO_TASK",
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"),
            joinColumns = @JoinColumn(name = "SCENARIO_ID"))
    private Collection<Task> tasks = new ArrayList<>();
}
