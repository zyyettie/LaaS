package org.g6.laas.server.database.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.LaaSPersistable;
import org.g6.laas.server.database.entity.task.ScenarioRunning;
import org.g6.laas.server.database.entity.task.Task;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "USERS", uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Users extends LaaSPersistable {

    private static final long serialVersionUID = -8503475553208415613L;

    @JsonView(UserDTO.class)
    @Column(name = "NAME")
    private String name;

    private String password;

    private String mail;

    @Column(name = "CREATE_TIME")
    @CreatedDate
    private Date createTime;

    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    @JsonView(UserDTO.class)
    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @OneToOne
    private Inbox inbox;

    @OneToMany(mappedBy = "createdBy")
    private Collection<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private Collection<Task> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private Collection<ScenarioRunning> taskRunning = new ArrayList<>();

    public Users(Long id) {
        super(id);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Role role = getRole();
        if (role != null) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String toString() {
        //satisfy UsernamepasswordAuthentionToken requirement.
        return this.name;
    }

    public interface UserDTO {
    }

    @JsonView(UserDTO.class)
    @Override
    public Long getId() {
        return super.getId();
    }
}
