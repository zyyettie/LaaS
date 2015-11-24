package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.ParameterDefine;
import org.g6.laas.server.database.entity.Product;
import org.g6.laas.server.database.entity.file.FileType;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TASK",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Task extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8503435553208415613L;

    private String name;

    private String className;

    @ManyToOne
    private Product product;

    private String description;

    @ManyToOne
    private FileType fileType;

    @ManyToMany
    private List<ParameterDefine> parameterDefines = new ArrayList<>();
}
