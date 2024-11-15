package com.example.btl.btl.models;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "email", columnDefinition = "VARCHAR(255)", unique = true)
    private String email;

    @Column(name = "phone", columnDefinition = "VARCHAR(10)", unique = true)
    private String phone;

    @Column(name = "address", columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(name = "username", columnDefinition = "VARCHAR(255)", unique = true)
    private String username;

    @Column(name = "password", columnDefinition = "VARCHAR(64)")
    private String password;

    @Column(name = "status", columnDefinition = "INT(1)")
    private int status;

    @Column(name = "createdBy", columnDefinition = "VARCHAR(36)")
    private String createdBy;

    @Column(name = "createdTime", columnDefinition = "TIMESTAMP")
    private Timestamp createdTime;

    @Column(name = "lastUpdatedBy", columnDefinition = "VARCHAR(36)")
    private String lastUpdatedBy;

    @Column(name = "lastUpdatedTime", columnDefinition = "TIMESTAMP")
    private Timestamp lastUpdatedTime;
}