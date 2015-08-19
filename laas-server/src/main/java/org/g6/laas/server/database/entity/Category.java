package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@Table(name = "CATEGORY",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
public class Category extends LaaSPersistable {
    private static final long serialVersionUID = -2951477269901540567L;

    @Column(name="NAME")
    private String name;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "categories")
    private Collection<Scenario> scenarios = new ArrayList<>();
}
