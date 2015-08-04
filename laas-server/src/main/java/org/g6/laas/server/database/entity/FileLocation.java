package org.g6.laas.server.database.entity;


import org.g6.laas.server.database.entity.user.User;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"FILE_NAME", "LOCATION"}))
public class FileLocation extends LaaSAuditable<User> {

    private static final long serialVersionUID = -8503475553208415619L;

    @Column(name = "FILE_NAME")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "FILE_TYPE_ID")
    private FileType type;

    @Column(name = "LOCATION")
    private String location;
}
