package org.g6.laas.server.database.entity;

import lombok.Data;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import org.joda.time.DateTime;

@Entity
@Data
@Table(name = "SCENARIO_HISTORY")
public class ScenarioHistory extends LaaSAuditable<User> {
    private static final long serialVersionUID = 3863174063361308807L;

    @ManyToOne
    @JoinColumn(name = "SCENARIO_ID")
    private Scenario scenario;

    private String information;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany
    @JoinTable(name="SCENARIO_INPUT_FILE",
            joinColumns={@JoinColumn(name="SCENARIO_ID")},
            inverseJoinColumns={@JoinColumn(name="FILE_ID")})
    private Collection<File> inputFiles = new ArrayList<>();

    @OneToMany(cascade=CascadeType.REFRESH)
    private Collection<File> outputFiles = new ArrayList<>();

    private DateTime duration;
}
