package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"TYPE"}))
@Data
@NoArgsConstructor
public class FileType extends LaaSPersistable{

    private static final long serialVersionUID = 8553475553208415613L;

    @Column(name="TYPE")
    private String type;

    public FileType(Long id){
        super(id);
    }
}
