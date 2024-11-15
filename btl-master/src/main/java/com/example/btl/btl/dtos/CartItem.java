package com.example.btl.btl.dtos;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Base64;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private int shoeId;
    private int shoeDetailId;
    private String title;
    private BigInteger price;
    private BigInteger promotePrice;
    private byte[] image;
    private int quantity;
    private String color;
    private int size;

    public String getFullDetailName() {
        return title + " " + size + "-" + color + " x " + quantity;
    }

     public String base64Image() {
        if (image != null) {
            if (image.length > 0) {
                return Base64.getEncoder().encodeToString(image);
            }
        }
        return "";
    }

    public String getPriceVND() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(price);
        return formatted + " VND";
    }

    public String getPromotePriceVND() {
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(promotePrice);
        return formatted + " VND";
    }

    public String getTotal() {
        BigInteger res = (this.promotePrice != null ? this.promotePrice : this.price)
                .multiply(BigInteger.valueOf((long) quantity));
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(res);
        return formatted + " VND";
    }
}
