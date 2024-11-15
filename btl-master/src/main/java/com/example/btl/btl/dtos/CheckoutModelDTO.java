package com.example.btl.btl.dtos;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutModelDTO {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String note;
    List<CartItem> cartItems;
}
