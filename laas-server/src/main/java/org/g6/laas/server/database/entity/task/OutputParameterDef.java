package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.Users;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class OutputParameterDef extends LaaSAuditable<Users> {
    private static final long serialVersionUID = 5446740475671050505L;

    private String name;
    private String dataType; //String, Integer, Object

    @ManyToOne
    private Task task;
}
