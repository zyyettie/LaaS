package org.g6.laas.server.database.entity.file;


import lombok.EqualsAndHashCode;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.LaaSAuditable;
import org.g6.laas.server.database.entity.user.User;
import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(mappedBy = "files")
    private List<Job> jobs = new ArrayList<>();

    @ManyToMany(mappedBy = "files")
    private List<JobRunning> jobRunnings = new ArrayList<>();
}
