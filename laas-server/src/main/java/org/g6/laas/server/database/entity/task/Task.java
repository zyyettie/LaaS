package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "TASK",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Task extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8503435553208415613L;

    @Column(name = "NAME")
    private String name;
    @Column(name = "CLASSNAME")
    private String className;

    @OneToMany(mappedBy = "task")
    private Collection<TaskRunning> taskRunnings = new ArrayList<>();
}
