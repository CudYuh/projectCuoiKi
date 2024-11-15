package com.example.btl.btl.models;

import java.sql.Timestamp;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {
    // 0 chưa xem
    // 1 đã xem
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(11)")
    private int id;

    @Column(name = "first_name", columnDefinition = "VARCHAR(15)")
    private String first_name;

    @Column(name = "last_name", columnDefinition = "VARCHAR(30)")
    private String last_name;

    @Column(name = "email", columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(name = "subject", columnDefinition = "VARCHAR(100)")
    private String subject;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", columnDefinition = "INT(1)")
    private int status;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;
}