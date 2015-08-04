package org.g6.laas.database.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners({AuditingEntityListener.class})
public class TaskHistory {
    @ManyToOne
    @JoinColumn(name="TASK_ID")
    private Task task;
    @ManyToOne
    @JoinColumn(name="USER_ID")
    @CreatedBy
    private User user;
    @Column(name="EXECUTE_TIME")
    @CreatedDate
    private Date executeTime;
    private Date duration;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }
}
