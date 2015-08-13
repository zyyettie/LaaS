package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@MappedSuperclass
public abstract class LaaSPersistable implements Serializable {

    private static final long serialVersionUID = -8503475553208415513L;

    @Id
    @GeneratedValue
    private Long id;

    @Transient
    public boolean isNew() {
        return null == getId();
    }

    public LaaSPersistable(Long id){
        this.id = id;
    }

}
