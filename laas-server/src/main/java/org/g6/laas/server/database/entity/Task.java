package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Task extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8503435553208415613L;

    @Column(name = "NAME")
    private String name;
    @Column(name = "CLASSNAME")
    private String className;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @CreatedBy
    private User user;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "tasks")
    private Collection<Workflow> workflows = new ArrayList<>();

    @OneToMany(mappedBy = "task")
    private Collection<TaskHistory> taskHistory = new ArrayList<>();
}
