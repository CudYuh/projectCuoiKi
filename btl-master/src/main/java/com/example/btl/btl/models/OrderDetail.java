package com.example.btl.btl.models;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import lombok.*;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(9)")
    private int id;

    @Column(name = "orderId", columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID orderId;

    @Column(name = "shoeId", columnDefinition = "INT(11)")
    private int shoeId;

    @Column(name = "price", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger price;

    @Column(name = "color", columnDefinition = "VARCHAR(255)")
    private String color;

    @Column(name = "size", columnDefinition = "INT(3)")
    private int size;

    @Column(name = "quantity", columnDefinition = "INT(11)")
    private int quantity;

    public String getPriceVND() {
        try {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(price);
            return formatted + " VND";
        } catch (Exception e) {
            return "Liên hệ";
        }
    }

    public String getTotalPriceVND() {
        try {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(price.multiply(BigInteger.valueOf((long) quantity)));
            return formatted + " VND";
        } catch (Exception e) {
            return "Liên hệ";
        }
    }
}