package org.g6.laas.server.database.entity.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.Users;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ScenarioResult extends LaaSAuditable<Users> {
    private static final long serialVersionUID = -5053098663559141920L;

    @OneToOne(cascade={CascadeType.ALL})
    private File file;
}
