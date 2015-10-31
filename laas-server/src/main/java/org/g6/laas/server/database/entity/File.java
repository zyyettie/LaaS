package org.g6.laas.server.database.entity;


import lombok.EqualsAndHashCode;
import org.g6.laas.server.database.entity.user.User;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "FILE",uniqueConstraints = @UniqueConstraint(columnNames = {"FILE_NAME", "PATH"}))
@Data
@EqualsAndHashCode(callSuper=false)
public class File extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8503475553208415619L;

    @Column(name = "FILE_NAME")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "FILE_TYPE_ID")
    private FileType type;

    @Column(name = "path")
    private String path;

    private String description;

    private String originalName;

    private Long size;

    private String isRemoved;
}
