package org.g6.laas.database.entity;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"FILE_NAME", "LOCATION"}))
@EntityListeners({AuditingEntityListener.class})
public class FileLocation {
    @Column(name="FILE_NAME")
    private String fileName;
    @ManyToOne
    @JoinColumn(name="FILE_TYPE_ID")
    private FileType type;
    @Column(name="LOCATION")
    private String location;
    @ManyToOne
    @JoinColumn(name="USER_ID")
    @CreatedBy
    private User user;
    @Column(name="CREATE_TIME")
    @CreatedDate
    private Date createTime;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
