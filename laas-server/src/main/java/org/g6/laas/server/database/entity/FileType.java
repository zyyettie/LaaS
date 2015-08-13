package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="FILE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = {"TYPE"}))
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
