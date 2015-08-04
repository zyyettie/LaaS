package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Role extends LaaSPersistable{

    private static final long serialVersionUID = -8503475553108415613L;

    @Column(name="NAME")
    private String name;

    public Role(Long id){
        super(id);
    }

    @OneToMany(mappedBy = "role")
    private Collection<User> users = new ArrayList<>();
}
