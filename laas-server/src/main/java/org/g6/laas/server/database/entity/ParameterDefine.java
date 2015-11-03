package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ParameterDefine extends LaaSAuditable<User> {
    private static final long serialVersionUID = -4095077639419164659L;

    private String name;
    private String description;

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
