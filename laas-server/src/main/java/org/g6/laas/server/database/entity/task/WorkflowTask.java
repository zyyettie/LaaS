package org.g6.laas.server.database.entity.task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class WorkflowTask extends LaaSAuditable<User> {

    @ManyToOne
    private Workflow workflow;

    @ManyToOne
    private Task task;

    @Column(name="task_order", nullable=false)
    private int order;
}
