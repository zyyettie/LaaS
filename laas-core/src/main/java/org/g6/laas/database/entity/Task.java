package org.g6.laas.database.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@EntityListeners({AuditingEntityListener.class})
public class Task {
    @Column(name="NAME")
    private String name;
    private String className;
    @ManyToOne
    @JoinColumn(name="USER_ID")
    @CreatedBy
    private User user;
    @OneToMany(mappedBy="task")
    private Collection<TaskHistory> taskHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection<TaskHistory> getTaskHistory() {
        return taskHistory;
    }

    public void setTask_history(Collection<TaskHistory> taskHistory) {
        this.taskHistory = taskHistory;
    }
}
