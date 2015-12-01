package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.result.ScenarioResult;
import org.g6.laas.server.database.entity.user.Users;

import javax.persistence.*;

@Entity
@Data
@Table
@EqualsAndHashCode(callSuper = false)
public class ScenarioRunning extends LaaSAuditable<Users> {

    private static final long serialVersionUID = -8603475553208415613L;

    @ManyToOne
    private Scenario scenario;

    @ManyToOne
    private JobRunning jobRunning;

    private String status;

    //For task failure
    private String rootCause;

    @OneToOne(cascade={CascadeType.ALL})
    private ScenarioResult result;

    //million second
    private Long duration;
}
