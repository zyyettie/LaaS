package org.g6.laas.server.database.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
public class User extends LaaSPersistable {

    private static final long serialVersionUID = -8503475553208415613L;

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
    private Collection<FileLocation> files = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private Collection<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy="user")
    private Collection<TaskHistory> taskHistory = new ArrayList<>();
}
