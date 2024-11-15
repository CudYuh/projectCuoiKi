package com.example.btl.btl.dtos;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.List;

import com.example.btl.btl.models.Order;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFullDetail {
    private Order order;
    private List<ShoeOrderDetail> orderDetails;

    public String getTotal() {
        BigInteger total = BigInteger.ZERO;
        for (ShoeOrderDetail item : orderDetails) {
            total = total.add(item.orderDetail.getPrice().multiply(BigInteger.valueOf((long) item.orderDetail.getQuantity())));
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        String formatted = formatter.format(total);
        return formatted + " VND";
    }
}
