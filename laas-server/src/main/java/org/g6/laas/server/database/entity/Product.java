package org.g6.laas.server.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.server.database.entity.user.Users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PRODUCT",uniqueConstraints = @UniqueConstraint(columnNames = {"NAME"}))
@Data
@NoArgsConstructor
public class Product extends LaaSAuditable<Users> {
    private static final long serialVersionUID = 692091866423602142L;

    @Column(name="NAME")
    private String name;
}
