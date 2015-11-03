package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class JobSubEntity extends LaaSNotifiable<User> {

    private static final long serialVersionUID = -1538015373412995204L;

    private String parameters;

    @ManyToMany
    private List<File> files = new ArrayList<>();

}
