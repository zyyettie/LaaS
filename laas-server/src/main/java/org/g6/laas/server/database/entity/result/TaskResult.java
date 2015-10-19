package org.g6.laas.server.database.entity.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.File;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskResult extends LaaSAuditable<User> {
    private static final long serialVersionUID = -5053098663559141920L;

    @OneToMany(cascade={CascadeType.PERSIST})
    private Collection<File> files = new ArrayList<>();
}
