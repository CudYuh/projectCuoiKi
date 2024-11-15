package com.example.btl.btl.dtos;

import java.util.Base64;

import com.example.btl.btl.models.OrderDetail;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoeOrderDetail {
    OrderDetail orderDetail;
    String title;
    byte[] image;
    public String base64Image(){
        return Base64.getEncoder().encodeToString(image);
    }
}
