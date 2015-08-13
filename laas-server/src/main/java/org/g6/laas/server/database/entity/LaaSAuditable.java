package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class LaaSAuditable<U> extends LaaSPersistable implements Auditable<U, Long> {

    private static final long serialVersionUID = 5479110990406241175L;

    @ManyToOne
    private U createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne
    private U lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    public LaaSAuditable(Long id) {
        super(id);
    }

    @Override
    public DateTime getCreatedDate() {
        return null == createdDate ? null : new DateTime(createdDate);
    }

    @Override
    public void setCreatedDate(final DateTime createdDate) {
        this.createdDate = null == createdDate ? null : createdDate.toDate();
    }

    @Override
    public DateTime getLastModifiedDate() {
        return null == lastModifiedDate ? null : new DateTime(lastModifiedDate);
    }

    @Override
    public void setLastModifiedDate(final DateTime lastModifiedDate) {
        this.lastModifiedDate = null == lastModifiedDate ? null : lastModifiedDate.toDate();
    }
}
