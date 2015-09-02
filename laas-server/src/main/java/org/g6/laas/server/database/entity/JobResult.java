package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
public class JobResult extends LaaSAuditable<User> {
    private static final long serialVersionUID = -2372791086285201685L;

    @OneToMany(cascade = CascadeType.REFRESH)
    private Collection<File> files = new ArrayList<>();
}
