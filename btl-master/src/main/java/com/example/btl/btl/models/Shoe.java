package com.example.btl.btl.models;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Base64;

import javax.persistence.*;

import lombok.*;

@Entity
@Table(name = "shoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shoe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(11)")
    private int id;

    @Column(name = "category_id", columnDefinition = "INT(11)")
    private int categoryId;

    @Column(name = "title", columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(name = "price", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger price;

    @Column(name = "promote_price", columnDefinition = "BIGINT UNSIGNED")
    private BigInteger promotePrice;

    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "status", columnDefinition = "INT(1)")
    private int status;

    @Column(name = "created_by", columnDefinition = "VARCHAR(36)")
    private String createdBy;

    @Column(name = "last_updated_by", columnDefinition = "VARCHAR(36)")
    private String lastUpdatedBy;

    @Column(name = "hot_order", columnDefinition = "INT(11)")
    private int hotOrder;

    public String base64Image() {
        if (image != null) {
            if (image.length > 0) {
                return Base64.getEncoder().encodeToString(image);
            }
        }
        return "";
    }

    public String getPriceVND() {
        try {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(price);
            return formatted + " VND";
        } catch (Exception e) {
            return "Liên hệ";
        }
    }

    public String getPromotePriceVND() {
        try {
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formatted = formatter.format(promotePrice);
            return formatted + " VND";
        } catch (Exception e) {
            return "Liên hệ";
        }
    }
}