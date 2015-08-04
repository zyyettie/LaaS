package org.g6.laas.database.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class User extends ID {
    @Column(name="NAME")
    private String name;
    private String password;
    private String mail;
    @Column(name="CREATE_TIME")
    @CreatedDate
    private Date createTime;
    @Column(name="LAST_LOGIN_TIME")
    private Date lastLoginTime;
    @ManyToOne
    @JoinColumn(name="ROLE_ID")
    private Role role;
    @OneToMany(mappedBy="user")
    private Collection<FileLocation> files;
    @OneToMany(mappedBy="user")
    private Collection<Task> tasks;
    @OneToMany(mappedBy="user")
    private Collection<TaskHistory> taskHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Collection<FileLocation> getFiles() {
        return files;
    }

    public void setFiles(Collection<FileLocation> files) {
        this.files = files;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Collection<TaskHistory> getTaskHistory() {
        return taskHistory;
    }

    public void setTaskHistory(Collection<TaskHistory> taskHistory) {
        this.taskHistory = taskHistory;
    }
}
