package com.example.btl.btl.models;

import java.sql.Timestamp;
import java.util.Base64;

import javax.persistence.*;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT(11)")
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "url", columnDefinition = "VARCHAR(255)", unique = true)
    private String url;

    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @Column(name = "createdBy", columnDefinition = "VARCHAR(36)")
    private String createdBy;

    @Column(name = "createdTime", columnDefinition = "TIMESTAMP")
    private Timestamp createdTime;

    @Column(name = "lastUpdatedBy", columnDefinition = "VARCHAR(36)")
    private String lastUpdatedBy;

    @Column(name = "lastUpdatedTime", columnDefinition = "TIMESTAMP")
    private Timestamp lastUpdatedTime;

    @Column(name = "status", columnDefinition = "INT(1)")
    private int status;

    @Transient
    private MultipartFile imageFile;

     public String base64Image() {
        if (image != null) {
            if (image.length > 0) {
                return Base64.getEncoder().encodeToString(image);
            }
        }
        return "";
    }
}