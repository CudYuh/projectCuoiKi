package com.example.btl.btl.models;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    // 0 chờ xác nhận
    // 1 đã xác nhận chờ giao
    // 2 đang giao
    // 3 giao thành công
    // -1 giao không thành công chờ hoàn trả
    // -2 giao không thành công đã hoàn trả
    // -3 hủy đơn

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "phone", columnDefinition = "VARCHAR(10)")
    private String phone;

    @Column(name = "address", columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "user_id", columnDefinition = "TEXT")
    private String userId;

    @Column(name = "createdTime", columnDefinition = "TIMESTAMP")
    private Timestamp createdTime;

    @Column(name = "status", columnDefinition = "INT(1)")
    private int status;

    @Column(name = "lastUpdatedBy", columnDefinition = "VARCHAR(36)")
    private String lastUpdatedBy;

    @Column(name = "lastUpdatedTime", columnDefinition = "TIMESTAMP")
    private Timestamp lastUpdatedTime;
}