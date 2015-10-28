package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class JobSubEntity extends LaaSNotifiable<User> {
    private static final long serialVersionUID = -1538015373412995204L;

    private String parameters;

    @ManyToMany
    private Collection<File> files = new ArrayList<>();

    @Override
    public Collection<User> sendTo() {
        return new ArrayList<>();
    }

    @Override
    public String getSummary() {
        return "";
    }
}
