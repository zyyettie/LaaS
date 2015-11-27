package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Parameter extends LaaSAuditable<User> {
    private String name;
    private String type; //String, Integer, Object

    @ManyToOne
    private Task input;
    @ManyToOne
    private Task output;
}
