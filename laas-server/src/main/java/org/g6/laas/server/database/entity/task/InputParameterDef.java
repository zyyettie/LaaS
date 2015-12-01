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
public class InputParameterDef extends LaaSAuditable<Users> {
    private static final long serialVersionUID = -4095077639419164659L;

    private String name;
    private String description;
    private String dataType;

    @ManyToOne
    private Task task;

    // Display information of UI
    private String displayInfo;
    private String type;
    private String displayList;
    private String valueList;
    private String defaultValue;
    private int width;
    private int height;
    private boolean lineOccupied;
}
