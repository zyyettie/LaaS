package org.g6.laas.database.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class ID {
    @Id
    @GeneratedValue
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
