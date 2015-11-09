package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import org.g6.laas.server.database.entity.Category;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.ParameterDefine;
import org.g6.laas.server.database.entity.Product;
import org.g6.laas.server.database.entity.file.FileType;
import org.g6.laas.server.database.entity.user.User;

import java.util.ArrayList;
import java.util.List;

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
    private List<Category> categories = new ArrayList<>();

    @ManyToMany(mappedBy = "scenarios")
    private List<Job> jobs = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "SCENARIO_TASK",
            inverseJoinColumns = @JoinColumn(name = "TASK_ID"),
            joinColumns = @JoinColumn(name = "SCENARIO_ID"))
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    private List<FileType> fileTypes = new ArrayList<>();

    @ManyToMany
    private List<ParameterDefine> parameterDefines = new ArrayList<>();
}
