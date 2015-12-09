package org.g6.laas.server.database.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name="ROLE",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Role extends LaaSPersistable {

    private static final long serialVersionUID = -8503475553108415613L;

    @JsonView(User.UserDTO.class)
    @Column(name="NAME")
    private String name;

    public Role(Long id){
        super(id);
    }

    @OneToMany(mappedBy = "role")
    private Collection<User> users = new ArrayList<>();

    @JsonView(User.UserDTO.class)
    @Override
    public Long getId() {
        return super.getId();
    }
}
