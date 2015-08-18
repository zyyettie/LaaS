package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Product extends LaaSAuditable<User> {
    private static final long serialVersionUID = 692091866423602142L;

    @Column(name="NAME")
    private String name;
}
