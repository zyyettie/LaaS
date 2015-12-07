package org.g6.laas.server.database.entity.user;

import lombok.Data;
import lombok.NonNull;
import org.g6.laas.server.database.entity.LaaSPersistable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Quota extends LaaSPersistable {
    private static final long serialVersionUID = -7002369595907189117L;

    @NonNull
    @OneToOne(mappedBy = "quota", cascade = {CascadeType.ALL})
    private User user;

    private long maxFileSize;

    private long SpaceQuota;

    private long usedSpace;
}
